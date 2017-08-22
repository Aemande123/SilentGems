package net.silentchaos512.gems.api.tool.part;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.tool.ToolStats;

public class ToolPartTip extends ToolPart {

  private int miningLevel;
  private int durabilityBoost;
  private float speedBoost;
  private float meleeBoost;
  private float magicBoost;

  protected ToolPartTip(String key, ItemStack craftingStack, int harvestLevel, int durabilityBoost,
      float speedBoost, float meleeDamage, float magicDamage) {

    super(key, craftingStack);
    this.miningLevel = harvestLevel;
    this.durabilityBoost = durabilityBoost;
    this.speedBoost = speedBoost;
    this.meleeBoost = meleeDamage;
    this.magicBoost = magicDamage;
  }

  @Override
  public void applyStats(ToolStats stats) {

    stats.durability += getDurability();
    stats.harvestSpeed += getHarvestSpeed();
    stats.meleeDamage += getMeleeDamage();
    stats.magicDamage += getMagicDamage();
    stats.meleeSpeed += getMeleeSpeed() - 1.0f;
    stats.enchantability += getEnchantability();
    stats.harvestLevel = Math.max(stats.harvestLevel, getHarvestLevel());
  }

  @Override
  public int getDurability() {

    return durabilityBoost;
  }

  @Override
  public float getHarvestSpeed() {

    return speedBoost;
  }

  @Override
  public int getHarvestLevel() {

    return miningLevel;
  }

  @Override
  public float getMeleeDamage() {

    return meleeBoost;
  }

  @Override
  public float getMagicDamage() {

    return magicBoost;
  }

  @Override
  public int getEnchantability() {

    return 0;
  }

  @Override
  public float getMeleeSpeed() {

    return 1.0f;
  }

  @Override
  public final float getProtection() {

    return 0;
  }
}
