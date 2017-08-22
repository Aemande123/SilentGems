package net.silentchaos512.gems.api;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.util.ToolHelper;

public interface ITool {

  public ItemStack constructTool(ItemStack rod, ItemStack head);

  public default float getMeleeDamage(ItemStack tool) {

    return getMeleeDamageModifier() + ToolHelper.getMeleeDamage(tool);
  }

  public default float getMagicDamage(ItemStack tool) {

    return getMagicDamageModifier() + ToolHelper.getMagicDamage(tool);
  }

  public float getMeleeDamageModifier();

  public float getMagicDamageModifier();

  public float getMeleeSpeedModifier();

  public default float getHarvestSpeedMultiplier() {

    return 1.0f;
  }

  public default float getDurabilityMultiplier() {

    return 1.0f;
  }

  public default float getRepairMultiplier() {

    return 1.0f;
  }

  public default boolean isDiggingTool() {

    return false;
  }

  public ConfigOptionToolClass getConfig();

  @Deprecated
  public default Material[] getExtraEffectiveMaterials() {

    return new Material[] {};
  }

  public default Material[] getExtraEffectiveMaterials(ItemStack tool) {

    return new Material[] {};
  }
}
