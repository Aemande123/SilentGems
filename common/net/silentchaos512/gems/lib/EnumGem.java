package net.silentchaos512.gems.lib;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.lib.util.StackHelper;

public enum EnumGem implements IStringSerializable {

  // @formatter:off
  //                                  DUR    EFF   MEL   MAG ENC   SPD PRT     COLOR
  RUBY            ("Ruby",            512,  8.0f, 6.0f, 2.0f, 10, 0.8f, 14, 0xFF0000),
  GARNET          ("Garnet",          384,  7.0f, 4.0f, 3.0f, 12, 0.9f, 16, 0xFF4000),
  TOPAZ           ("Topaz",           384,  9.0f, 4.0f, 2.0f, 12, 0.8f, 20, 0xFF6A00),
  AMBER           ("Amber",           128,  5.0f, 2.0f, 4.0f, 17, 1.1f, 12, 0xFFAA00),
  HELIODOR        ("Heliodor",        256, 12.0f, 4.0f, 3.0f, 10, 1.0f, 12, 0xFFD500),
  PERIDOT         ("Peridot",         256,  6.0f, 4.0f, 3.0f, 14, 0.7f, 14, 0xAAFF00),
  BERYL           ("Beryl",           384,  9.0f, 4.0f, 2.0f, 16, 1.1f, 14, 0x00FF00),
  INDICOLITE      ("Indicolite",      384, 10.0f, 2.0f, 5.0f, 12, 1.0f, 12, 0x00FF80),
  AQUAMARINE      ("Aquamarine",      256,  9.0f, 3.0f, 4.0f, 12, 1.1f, 14, 0x00FFFF),
  SAPPHIRE        ("Sapphire",        512,  8.0f, 4.0f, 4.0f, 10, 0.8f, 16, 0x0000FF),
  IOLITE          ("Iolite",          512,  6.0f, 2.0f, 4.0f, 11, 1.0f, 20, 0x5500FF),
  AMETHYST        ("Amethyst",        256,  7.0f, 3.0f, 4.0f, 12, 0.9f, 18, 0xAA00FF),
  AGATE           ("Agate",           192,  8.0f, 3.0f, 3.0f, 14, 1.1f, 16, 0xFF00FF),
  MORGANITE       ("Morganite",       256, 10.0f, 4.0f, 2.0f, 12, 0.9f, 14, 0xFFABE3),
  ONYX            ("Onyx",            128,  8.0f, 7.0f, 2.0f,  8, 0.7f, 12, 0x2E2E2E),
  OPAL            ("Opal",            192,  8.0f, 3.0f, 6.0f, 13, 0.7f, 16, 0xFFFFFF),
  //----------------------------------------------------------------------------------
  CARNELIAN       ("Carnelian",       256,  9.0f, 2.0f, 3.0f, 12, 0.9f, 14, 0x990000),
  SPINEL          ("Spinel",          512,  8.0f, 5.0f, 2.0f, 11, 0.7f, 16, 0x992600),
  CITRINE         ("Citrine",         384, 10.0f, 4.0f, 2.0f, 13, 1.0f, 14, 0x994000),
  JASPER          ("Jasper",          256,  7.0f, 3.0f, 3.0f, 14, 0.9f, 18, 0x996600),
  GOLDEN_BERYL    ("GoldenBeryl",     384, 10.0f, 2.0f, 5.0f, 10, 0.7f, 12, 0x998000),
  MOLDAVITE       ("Moldavite",       192,  6.0f, 5.0f, 2.0f, 11, 0.8f, 16, 0x669900),
  MALACHITE       ("Malachite",       128,  8.0f, 4.0f, 2.0f, 14, 1.3f, 14, 0x009900),
  TURQUOISE       ("Turquoise",       256,  9.0f, 3.0f, 3.0f, 12, 0.8f, 14, 0x00994D),
  MOONSTONE       ("Moonstone",       256,  9.0f, 3.0f, 5.0f, 15, 1.0f, 14, 0x009999),
  BLUE_TOPAZ      ("BlueTopaz",       512,  9.0f, 3.0f, 3.0f, 11, 0.7f, 16, 0x000099),
  TANZANITE       ("Tanzanite",       384,  6.0f, 3.0f, 4.0f, 13, 0.7f, 16, 0x330099),
  VIOLET_SAPPHIRE ("VioletSapphire",  512,  8.0f, 4.0f, 3.0f, 11, 0.9f, 16, 0x660099),
  LEPIDOLITE      ("Lepidolite",      128,  4.0f, 3.0f, 7.0f, 13, 1.0f, 12, 0x990099),
  AMETRINE        ("Ametrine",        384,  8.0f, 4.0f, 2.0f, 10, 0.7f, 12, 0xBF0080),
  BLACK_DIAMOND   ("BlackDiamond",    768, 10.0f, 3.0f, 4.0f,  9, 0.8f, 18, 0x1A1A1A),
  ALEXANDRITE     ("Alexandrite",     512,  8.0f, 3.0f, 3.0f, 10, 0.8f, 14, 0xC2C2C2),
  //----------------------------------------------------------------------------------
  PYROPE          ("Pyrope",          384,  8.0f, 6.0f, 2.0f, 12, 1.0f, 16, 0xFF6A6A),
  CORAL           ("Coral",           192,  9.0f, 3.0f, 5.0f, 18, 1.2f, 20, 0xFF966A),
  SUNSTONE        ("Sunstone",        256,  7.0f, 5.0f, 5.0f, 16, 0.9f, 16, 0xFFB46A),
  CATS_EYE        ("CatsEye",         512,  9.0f, 3.0f, 4.0f, 16, 1.1f, 18, 0xFFE06A),
  ZIRCON          ("Zircon",          384,  8.0f, 4.0f, 3.0f, 12, 1.3f, 14, 0xFFFE6A),
  JADE            ("Jade",            256,  6.0f, 4.0f, 4.0f, 16, 1.0f, 16, 0xE0FF6A),
  CHRYSOPRASE     ("Chrysoprase",     384,  7.0f, 3.0f, 2.0f, 14, 1.1f, 18, 0x6AFF6A),
  APATITE         ("Apatite",         192,  7.0f, 2.0f, 4.0f, 15, 1.0f, 16, 0x6AFFC3),
  FLUORITE        ("Fluorite",        128,  6.0f, 2.0f, 5.0f, 17, 0.9f, 14, 0x6AFFFF),
  KYANITE         ("Kyanite",         384, 12.0f, 4.0f, 6.0f, 16, 1.2f, 16, 0x6A6AFF),
  SODALITE        ("Sodalite",        384,  8.0f, 3.0f, 3.0f, 12, 1.1f, 18, 0xA56AFF),
  AMMOLITE        ("Ammolite",        192,  7.0f, 3.0f, 6.0f, 18, 1.2f, 14, 0xE06AFF),
  KUNZITE         ("Kunzite",         256,  6.0f, 5.0f, 4.0f, 14, 0.8f, 20, 0xFF6AFF),
  ROSE_QUARTZ     ("RoseQuartz",      256,  8.0f, 4.0f, 3.0f, 15, 1.0f, 16, 0xFFBAFF),
  TEKTITE         ("Tektite",         192,  8.0f, 4.0f, 3.0f, 17, 1.0f, 18, 0x787878),
  PEARL           ("Pearl",           128,  7.0f, 3.0f, 4.0f, 20, 1.2f, 14, 0xFFFFFF);
  // @formatter:on

  public static final PropertyEnum VARIANT_GEM = PropertyEnum.create("gem", EnumGem.class, RUBY,
      GARNET, TOPAZ, AMBER, HELIODOR, PERIDOT, BERYL, INDICOLITE, AQUAMARINE, SAPPHIRE, IOLITE,
      AMETHYST, AGATE, MORGANITE, ONYX, OPAL);

  public static final int REGULAR_HARVEST_LEVEL = 2;
  public static final int SUPER_HARVEST_LEVEL = 4;
  public static final int SUPER_DURABILITY_MULTI = 4;
  public static final float SUPER_MINING_SPEED_BOOST = 4.0f;
  public static final float SUPER_MELEE_DAMAGE_BOOST = 3.0f;
  public static final float SUPER_MAGIC_DAMAGE_BOOST = 3.0f;
  public static final int SUPER_ENCHANTABILITY_BOOST = 8;
  public static final float SUPER_MELEE_SPEED_BOOST = 0.2f;
  public static final int SUPER_PROTECTION_BOOST = 4;

  protected static final String STR_SUPER = "Super";

  protected final String name;
  protected final int durability;
  protected final float miningSpeed;
  protected final float meleeDamage;
  protected final float magicDamage;
  protected final float meleeSpeed;
  protected final int enchantability;
  protected final int protection;
  protected final int color;

  private EnumGem(String name, int durability, float miningSpeed, float meleeDamage,
      float magicDamage, int enchantability, float meleeSpeed, int protection, int color) {

    this.name = name;
    this.durability = durability;
    this.miningSpeed = miningSpeed;
    this.meleeDamage = meleeDamage;
    this.magicDamage = magicDamage;
    this.meleeSpeed = meleeSpeed;
    this.enchantability = enchantability;
    this.protection = protection;
    this.color = color;
  }

  /**
   * @return The IStringSerializable name: All lowercase with underscores, good for block states.
   */
  @Override
  public String getName() {

    return name().toLowerCase();
  }

  /**
   * @return A localization-friendly version of the name, capital case with no spaces or underscores.
   */
  public String getGemName() {

    return name;
  }

  // ===========================
  // Tier-sensitive stat getters
  // ===========================

  public int getDurability(boolean supercharged) {

    return supercharged ? durability * SUPER_DURABILITY_MULTI : durability;
  }

  public float getMiningSpeed(boolean supercharged) {

    return supercharged ? miningSpeed + SUPER_MINING_SPEED_BOOST : miningSpeed;
  }

  public float getMeleeDamage(boolean supercharged) {

    return supercharged ? meleeDamage + SUPER_MELEE_DAMAGE_BOOST : meleeDamage;
  }

  public float getMagicDamage(boolean supercharged) {

    return supercharged ? magicDamage + SUPER_MAGIC_DAMAGE_BOOST : magicDamage;
  }

  public int getEnchantability(boolean supercharged) {

    return supercharged ? enchantability + SUPER_ENCHANTABILITY_BOOST : enchantability;
  }

  public float getMeleeSpeed(boolean supercharged) {

    return supercharged ? meleeSpeed + SUPER_MELEE_SPEED_BOOST : meleeSpeed;
  }

  public int getProtection(boolean supercharged) {

    return supercharged ? protection + SUPER_PROTECTION_BOOST : protection;
  }

  public int getHarvestLevel(boolean supercharged) {

    return supercharged ? SUPER_HARVEST_LEVEL : REGULAR_HARVEST_LEVEL;
  }

  public int getColor() {

    return color;
  }

  public static EnumGem getFromStack(ItemStack stack) {

    if (StackHelper.isEmpty(stack) || stack.getItem() != ModItems.gem) {
      return null;
    }

    return values()[MathHelper.clamp(stack.getItemDamage(), 0, values().length - 1)];
  }

  public static EnumGem getRandom() {

    return values()[SilentGems.random.nextInt(values().length)];
  }

  // ======================
  // Block and Item getters
  // ======================

  /**
   * @return The gem block.
   */
  public ItemStack getBlock() {

    return new ItemStack(ordinal() < 16 ? ModBlocks.gemBlock : ModBlocks.gemBlockDark, 1,
        ordinal() & 0xF);
  }

  /**
   * @return The ore dictionary name for the gem block.
   */
  public String getBlockOreName() {

    return "block" + name;
  }

  /**
   * @return The supercharged gem block.
   */
  public ItemStack getBlockSuper() {

    return new ItemStack(ordinal() < 16 ? ModBlocks.gemBlockSuper : ModBlocks.gemBlockSuperDark, 1,
        ordinal() & 0xF);
  }

  /**
   * @return The ore dictionary name for the supercharged gem block.
   */
  public String getBlockSuperOreName() {

    return getBlockOreName() + STR_SUPER;
  }

  /**
   * @return The gem ore block.
   */
  public ItemStack getOre() {

    return new ItemStack(ordinal() < 16 ? ModBlocks.gemOre : ModBlocks.gemOreDark, 1,
        ordinal() & 0xF);
  }

  /**
   * @return The ore dictionary name for the gem ore block.
   */
  public String getOreOreName() {

    return "ore" + name;
  }

  /**
   * @return The gem item.
   */
  public ItemStack getItem() {

    return new ItemStack(ModItems.gem, 1, ordinal());
  }

  /**
   * @return The ore dictionary name for the gem item.
   */
  public String getItemOreName() {

    return "gem" + name;
  }

  /**
   * @return The supercharged gem item.
   */
  public ItemStack getItemSuper() {

    return new ItemStack(ModItems.gemSuper, 1, ordinal());
  }

  /**
   * @return The ore dictionary name for the supercharged gem item.
   */
  public String getItemSuperOreName() {

    return getItemOreName() + STR_SUPER;
  }

  /**
   * @return The gem shard (nugget) item.
   */
  public ItemStack getShard() {

    return new ItemStack(ModItems.gemShard, 1, ordinal());
  }

  /**
   * @return The ore dictionary name of the gem shard (nugget) item.
   */
  public String getShardOreName() {

    return "nugget" + name;
  }
}
