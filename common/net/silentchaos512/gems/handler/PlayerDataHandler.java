package net.silentchaos512.gems.handler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.network.NetworkHandler;
import net.silentchaos512.gems.network.message.MessageDataSync;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.tile.SyncVariable;

// Lots of inspiration from Psi here. Previous version of the mod used IExtendedEntityProperties,
// but that doesn't seem to work in 1.9.
public class PlayerDataHandler {

  public static final String NBT_ROOT = SilentGems.MODID_NBT + "Data";

  private static Map<Integer, PlayerData> playerData = new HashMap();

  public static PlayerData get(EntityPlayer player) {

    int key = getKey(player);
    if (!playerData.containsKey(key)) {
      playerData.put(key, new PlayerData(player));
    }

    PlayerData data = playerData.get(key);
    if (data.playerWR.get() != player) {
      NBTTagCompound tags = new NBTTagCompound();
      data.writeToNBT(tags);
      playerData.remove(key);
      data = get(player);
      data.readFromNBT(tags);
    }

    return data;
  }

  public static void cleanup() {

    List<Integer> remove = new ArrayList();

    for (int i : playerData.keySet()) {
      PlayerData d = playerData.get(i);
      if (d != null && d.playerWR.get() == null) {
        remove.add(i);
      }
    }

    for (int i : remove) {
      playerData.remove(i);
    }
  }

  private static int getKey(EntityPlayer player) {

    return player.hashCode() << 1 + (player.world.isRemote ? 1 : 0);
  }

  public static NBTTagCompound getDataCompoundForPlayer(EntityPlayer player) {

    NBTTagCompound forgeData = player.getEntityData();
    if (!forgeData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
      forgeData.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
    }

    NBTTagCompound persistentData = forgeData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
    if (!persistentData.hasKey(NBT_ROOT)) {
      persistentData.setTag(NBT_ROOT, new NBTTagCompound());
    }

    return persistentData.getCompoundTag(NBT_ROOT);
  }

  public static class EventHandler {

    @SubscribeEvent
    public void onServerTick(ServerTickEvent event) {

      if (event.phase == Phase.END) {
        PlayerDataHandler.cleanup();
      }
    }

    final int SOUL_TICK_DELAY = 4 * 1200;
    final int SOUL_TICK_SALT = 10 + SilentGems.random.nextInt(60);

    @SubscribeEvent
    public void onPlayerTick(LivingUpdateEvent event) {

      if (event.getEntityLiving() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        PlayerDataHandler.get(player).tick();

        // Save tool souls
        if ((player.world.getTotalWorldTime() + SOUL_TICK_SALT) % SOUL_TICK_DELAY == 0) {
          ToolHelper.writeToolSoulsToNBT(player);
        }
      }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {

      if (event.player instanceof EntityPlayerMP) {
        MessageDataSync message = new MessageDataSync(get(event.player));
        NetworkHandler.INSTANCE.sendTo(message, (EntityPlayerMP) event.player);
      }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerLoggedOutEvent event) {

      // Save the players tool souls before logging out.
      ToolHelper.writeToolSoulsToNBT(event.player);
      // Wipe the tool soul map (could probably just wipe the player's souls, but do all to be safe).
      ToolHelper.clearToolSoulMap();
    }
  }

  public static class PlayerData {

    public static final int CHAOS_MAX_TRANSFER = 1000;
    public static final int RECHARGE_COOLDOWN_TIME = 100;
    public static final int HALO_TIME_DEFAULT = 12000;

    @SyncVariable(name = "Chaos")
    public int chaos;
    @SyncVariable(name = "MaxChaos")
    public int maxChaos;
    @SyncVariable(name = "RechargeCooldown")
    public int rechargeCooldown;
    @SyncVariable(name = "FlightTime", onRead = false)
    public int flightTime = 200;
    @SyncVariable(name = "StarterSoulsGiven")
    public boolean starterToolSoulsGiven;

    public int magicCooldown;

    public WeakReference<EntityPlayer> playerWR;
    private final boolean client;

    public PlayerData(EntityPlayer player) {

      playerWR = new WeakReference<EntityPlayer>(player);
      client = player.world.isRemote;

      load();
    }

    public void tick() {

      EntityPlayer player = playerWR.get();

      if (maxChaos == 0) {
        maxChaos = 10000;
      }

      boolean shouldSave = false;

      // Magic cooldown?
      if (magicCooldown > 0) {
        --magicCooldown;
        shouldSave = true;
      }

      // Recharge cooldown?
      if (rechargeCooldown > 0) {
        --rechargeCooldown;
        shouldSave = true;
      }

      // Flight timer (fix infinite flight)
      if (flightTime > 0) {
        --flightTime;
        if (flightTime == 0 && !player.capabilities.isCreativeMode) {
          player.capabilities.allowFlying = false;
          player.capabilities.isFlying = false;
        }
        shouldSave = true;
      }

      if (shouldSave)
        save();
    }

    public void drainChaos(int amount) {

      if (amount == 0)
        return;

      chaos = Math.max(chaos - amount, 0);
      rechargeCooldown = RECHARGE_COOLDOWN_TIME;
      save();
      sendUpdateMessage();
    }

    public int sendChaos(int amount) {

      return sendChaos(amount, false);
    }

    public int sendChaos(int amount, boolean ignoreCooldown) {

      if (rechargeCooldown > 0 && !ignoreCooldown) {
        return 0;
      }

      int pulled = Math.min(maxChaos - chaos, amount);
      chaos += pulled;
      // SilentGems.instance.logHelper.debug(client, pulled, chaos, maxChaos);

      if (pulled > 0) {
        save();
        sendUpdateMessage();
      }

      return pulled;
    }

    private void sendUpdateMessage() {

      if (!client) {
        EntityPlayer player = playerWR.get();
        MessageDataSync message = new MessageDataSync(get(player));
        NetworkHandler.INSTANCE.sendTo(message, (EntityPlayerMP) player);
      }
    }

    public int getCurrentChaos() {

      return chaos;
    }

    public int getMaxChaos() {

      return maxChaos;
    }

    public int getChaosChargeSpeed() {

      EntityPlayer player = playerWR.get();
      ItemStack mainHand = player.getHeldItem(EnumHand.MAIN_HAND);
      ItemStack offHand = player.getHeldItem(EnumHand.OFF_HAND);

      float multi = 1.0f; // TODO: chaos charge speed multi?

      // SilentGems.instance.logHelper.debug(multi);
      return (int) (multi * CHAOS_MAX_TRANSFER);
    }

    public void save() {

      if (!client) {
        EntityPlayer player = playerWR.get();

        if (player != null) {
          NBTTagCompound tags = getDataCompoundForPlayer(player);
          writeToNBT(tags);
        }
      }
    }

    public void writeToNBT(NBTTagCompound tags) {

      SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.WRITE);

      // tags.setInteger(NBT_CHAOS, chaos);
      // tags.setInteger(NBT_MAX_CHAOS, maxChaos);
      // tags.setInteger(NBT_RECHARGE_COOLDOWN, rechargeCooldown);
      // tags.setShort(NBT_FLIGHT_TIME, (short) flightTime);
    }

    public void load() {

      if (!client) {
        EntityPlayer player = playerWR.get();

        if (player != null) {
          NBTTagCompound tags = getDataCompoundForPlayer(player);
          readFromNBT(tags);
        }
      }
    }

    public void readFromNBT(NBTTagCompound tags) {

      SyncVariable.Helper.readSyncVars(this, tags);

      // chaos = tags.getInteger(NBT_CHAOS);
      // maxChaos = tags.getInteger(NBT_MAX_CHAOS);
      // rechargeCooldown = tags.getInteger(NBT_RECHARGE_COOLDOWN);
    }
  }
}
