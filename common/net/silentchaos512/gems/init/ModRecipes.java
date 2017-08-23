package net.silentchaos512.gems.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.silentchaos512.gems.api.SilentGemsAPI;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.recipe.RecipeApplyEnchantmentToken;
import net.silentchaos512.gems.recipe.RecipeChaosGemUpgrade;
import net.silentchaos512.gems.recipe.RecipeCraftTool;
import net.silentchaos512.gems.recipe.RecipeHoldingGemSetBlock;
import net.silentchaos512.gems.recipe.RecipeNamePlate;
import net.silentchaos512.lib.registry.IRegistrationHandler;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.registry.SRegistry;

public class ModRecipes implements IRegistrationHandler<IRecipe> {

  @Override
  public void registerAll(SRegistry reg) {

    RecipeMaker recipes = reg.recipes;

    // Chaos Essence creation.
    SilentGemsAPI.addAltarRecipe(ModItems.craftingMaterial.chaosEssence,
        ModItems.craftingMaterial.getStack(Names.CHAOS_ESSENCE_SHARD, 4), 240000,
        new ItemStack(Items.DIAMOND));

    // Light <--> Dark gem conversion.
    ItemStack slimeBall = new ItemStack(Items.SLIME_BALL);
    ItemStack magmaCream = new ItemStack(Items.MAGMA_CREAM);
    for (int i = 0; i < 16; ++i) {
      EnumGem light = EnumGem.values()[i];
      EnumGem dark = EnumGem.values()[i + 16];
      ItemStack lightShards = new ItemStack(ModItems.gemShard, 6, light.ordinal());
      ItemStack darkShards = new ItemStack(ModItems.gemShard, 6, dark.ordinal());
      SilentGemsAPI.addAltarRecipe(darkShards, light.getItem(), 80000, magmaCream);
      SilentGemsAPI.addAltarRecipe(lightShards, dark.getItem(), 80000, slimeBall);
    }

    // Recipe handlers.
    recipes.addCustomRecipe("craft_tool", new RecipeCraftTool());
//    recipes.addCustomRecipe("multipart_tool", new RecipeMultiGemTool());
//    recipes.addCustomRecipe("multipart_shield", new RecipeMultiGemShield());
//    recipes.addCustomRecipe("multipart_bow", new RecipeMultiGemBow());
//    recipes.addCustomRecipe("multipart_armor", new RecipeMultiGemArmor());
//    recipes.addCustomRecipe("decorate_tool", new RecipeDecorateTool());
//    recipes.addCustomRecipe("decorate_armor", new RecipeDecorateArmor());
    recipes.addCustomRecipe("apply_enchantment_token", new RecipeApplyEnchantmentToken());
    recipes.addCustomRecipe("chaos_gem_upgrade", new RecipeChaosGemUpgrade());
    recipes.addCustomRecipe("name_plate_use", new RecipeNamePlate());
    recipes.addCustomRecipe("holding_gem_set_block", new RecipeHoldingGemSetBlock());
  }
}
