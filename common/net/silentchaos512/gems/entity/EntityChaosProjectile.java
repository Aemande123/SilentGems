package net.silentchaos512.gems.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.gems.lib.part.ToolPartGem;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.Color;

public class EntityChaosProjectile extends EntityThrowable implements IEntityAdditionalSpawnData {

  public static final double RENDER_DISTANCE_WEIGHT = 10D;
  public static final float SIZE = 1.0F;

  public static final String NBT_COLOR = "Color";
  public static final String NBT_DAMAGE = "Damage";
  public static final String NBT_GRAVITY = "Gravity";
  public static final String NBT_SHOOTER = "Shooter";

  protected EntityLivingBase shooter;
  // protected ItemStack castingStack;
  private Color color = Color.WHITE;
  protected float damage = 0f;
  protected boolean gravity = true;
  protected int bounces = 4;

  public EntityChaosProjectile(World worldIn) {

    super(worldIn);
    // setRenderDistanceWeight(RENDER_DISTANCE_WEIGHT);
    setSize(SIZE, SIZE);
  }

  public EntityChaosProjectile(EntityLivingBase shooter, ItemStack castingStack, float damage) {

    super(shooter.world, shooter);

    this.shooter = shooter;
    this.damage = damage;
    // this.castingStack = castingStack;

    setSize(SIZE, SIZE);

    Vec3d vec = shooter.getLookVec();
    motionX = vec.x;
    motionY = vec.y;
    motionZ = vec.z;

    float speedMulti = 0.7f;
    motionX += shooter.motionX;
    motionY += shooter.motionY;
    motionZ += shooter.motionZ;
    motionX *= speedMulti;
    motionY *= speedMulti;
    motionZ *= speedMulti;
    // this.gravity = gravity;

    // Color
    ToolPart part = ToolHelper.getRenderPart(castingStack, EnumPartPosition.HEAD);
    if (part instanceof ToolPartGem) {
      ToolPartGem partGem = (ToolPartGem) part;
      this.color = new Color(partGem.getGem().getColor());
    } else if (part != null) {
      this.color = new Color(part.getColor(castingStack));
    }

    // SilentGems.instance.logHelper.debug(posX, posY, posZ, motionX, motionY, motionZ);

    // gemId = gemId == -1 ? ModMaterials.CHAOS_GEM_ID : gemId;
    // if (gemId >= 0 && gemId < COLORS.length) {
    // setColor(COLORS[gemId]);
    // } else {
    // setColor(0xFFFFFF);
    // LogHelper.warning("EntityProjectileChaosOrb (constructor): Unknown gemId! " + gemId);
    // }

    posX += motionX;
    posY += motionY;
    posZ += motionZ;
  }

  public int getMaxLife() {

    return 200;
  }

  @Override
  public void onUpdate() {

    super.onUpdate();

    // if (homingTarget != null && homingTarget.isDead) {
    // findHomingTarget();
    // }

    Vec3d vec1 = new Vec3d(posX, posY, posZ);
    Vec3d vec2 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
    RayTraceResult mop = world.rayTraceBlocks(vec1, vec2, false, true, false);
    if (mop != null) {
      onImpact(mop);
    }

    if (ticksExisted > getMaxLife()) {
      setDead();
    }

    // Body particle
    SilentGems.proxy.spawnParticles(EnumModParticles.CHAOS_PROJECTILE_BODY, getColor(), world, posX,
        posY, posZ, 0, 0, 0);
    // Tail particles
    if (ticksExisted > 2) {
      for (int i = 0; i < 1 + 3 / (1 + 2 * SilentGems.instance.proxy.getParticleSettings()); ++i) {
        double mx = world.rand.nextGaussian() * 0.01f;
        double my = world.rand.nextGaussian() * 0.01f;
        double mz = world.rand.nextGaussian() * 0.01f;
        SilentGems.proxy.spawnParticles(EnumModParticles.CHAOS, getColor(), world, posX, posY, posZ,
            mx, my, mz);
      }
    }
  }

  @Override
  protected void onImpact(RayTraceResult mop) {

    BlockPos posHit = mop.getBlockPos();
    if (mop.typeOfHit == Type.ENTITY && shooter != null && mop.entityHit != shooter) {
      // Collide with Entity?
      mop.entityHit.attackEntityFrom(
          DamageSource.causeIndirectMagicDamage(getShooter(), getShooter()), damage);
      // if (castingStack != null) {
      // ToolHelper.incrementStatShotsLanded(castingStack, 1);
      // }
      // worldObj.createExplosion(this, posX, posY, posZ, 1.25f, false);
      setDead();
    } else if (mop.typeOfHit == Type.BLOCK
        && canCollideWithBlock(world.getBlockState(posHit), posHit)) {
      // Collide with Block?
      BlockPos pos = mop.getBlockPos();
      IBlockState state = world.getBlockState(pos);
      Block block = state.getBlock();
      AxisAlignedBB boundingBox = state.getBoundingBox(world, pos);

      // Bounce off of blocks that can be collided with.
      if (bounces > 0 && boundingBox != null) {
        --bounces;
        switch (mop.sideHit) {
          case UP:
          case DOWN:
            motionY *= -0.95;
            break;
          case NORTH:
          case SOUTH:
            motionZ *= -0.95;
            break;
          case EAST:
          case WEST:
            motionX *= -0.95;
            break;
        }
        spawnHitParticles(16);
        world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.AMBIENT, 0.5f,
            0.65f);
      } else if (boundingBox != null) {
        setDead();
      }
    }
  }

  protected boolean canCollideWithBlock(IBlockState state, BlockPos pos) {

    Material mat = state.getMaterial();
    if (mat == Material.AIR || mat == Material.PLANTS || mat == Material.VINE) {
      return false;
    }
    return state.getBoundingBox(world, pos) != null;
  }

  @Override
  public float getGravityVelocity() {

    return gravity ? 0.02f : 0f;
  }

  @Override
  public void setDead() {

    spawnHitParticles(64);
    float f = (float) (0.75f + rand.nextGaussian() * 0.05f);
    world.playSound(null, getPosition(), SoundEvents.ENTITY_FIREWORK_BLAST, SoundCategory.AMBIENT,
        0.75f, f);

    super.setDead();
  }

  protected void spawnHitParticles(int count) {

    double mX, mY, mZ;
    for (int i = 0; i < count; ++i) {
      mX = rand.nextGaussian() * 0.05;
      mY = rand.nextGaussian() * 0.05;
      mZ = rand.nextGaussian() * 0.05;
      world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY, posZ, mX, mY, mZ);
    }
  }

  @Override
  protected void setOnFireFromLava() {

  }

  @Override
  public void setPortal(BlockPos pos) {

  }

  public Color getColor() {

    return color;
  }

  public EntityChaosProjectile setColor(Color color) {

    this.color = color;
    return this;
  }

  public EntityChaosProjectile setDamage(float damage) {

    this.damage = damage;
    return this;
  }

  public EntityLivingBase getShooter() {

    return shooter;
  }

  @Override
  public void readEntityFromNBT(NBTTagCompound tags) {

    super.readEntityFromNBT(tags);

    setColor(new Color(tags.getInteger(NBT_COLOR)));
    damage = tags.getFloat(NBT_DAMAGE);
    gravity = tags.getBoolean(NBT_GRAVITY);
    if (tags.hasKey(NBT_SHOOTER)) {
      shooter = world.getPlayerEntityByName(tags.getString(NBT_SHOOTER));
      if (shooter == null) {
        setDead();
      }
    }
  }

  @Override
  public void writeEntityToNBT(NBTTagCompound tags) {

    super.writeEntityToNBT(tags);

    tags.setInteger(NBT_COLOR, color.getColor());
    tags.setFloat(NBT_DAMAGE, damage);
    tags.setBoolean(NBT_GRAVITY, gravity);
    if (shooter != null) {
      tags.setString(NBT_SHOOTER, shooter.getName());
    }
  }

  @Override
  public void readSpawnData(ByteBuf data) {

    try {
      color = new Color(data.readInt());
      damage = data.readFloat();
      gravity = data.readBoolean();
    } catch (Exception ex) {
    }
  }

  @Override
  public void writeSpawnData(ByteBuf data) {

    data.writeInt(color.getColor());
    data.writeFloat(damage);
    data.writeBoolean(gravity);
  }
}
