package net.silentchaos512.gems.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.silentchaos512.gems.SilentGems;

public class PotionSlowFall extends Potion {

  public static final double SLOW_FALL_SPEED = -0.5;

  public PotionSlowFall() {

    super(false, 0x4286f4);
    setPotionName(SilentGems.RESOURCE_PREFIX + "effect.slow_fall");
    setBeneficial();
  }

  @Override
  public void performEffect(EntityLivingBase entityLiving, int amplifier) {

    SilentGems.logHelper.debug(entityLiving.motionY);
    if (entityLiving.motionY < SLOW_FALL_SPEED) {
      entityLiving.motionY = SLOW_FALL_SPEED;
      entityLiving.fallDistance = 0;
    }
  }

  @Override
  public boolean isReady(int duration, int amplifier) {

    return true;
  }
}
