package com.aoimod.datagen;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.blocks.ModBlocks;
import com.aoimod.recipes.CampfireRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeGenerator;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    private static class ModRecipeGenerator extends RecipeGenerator {
        protected ModRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
            super(registries, exporter);
        }

        @Override
        public void generate() {
            offerShapelessRecipe(Items.STICK, ModBlocks.TWIG, "twig", 1);
            offerCampfireRecipe(Blocks.OAK_LOG, Items.CHARCOAL, 1, "test_recipe");
        }

        public void offerCampfireRecipe(ItemConvertible input, ItemConvertible output, int count, String id) {
            Ingredient i = Ingredient.ofItem(input);
            ItemStack o = new ItemStack(output, count);
            Identifier d = Identifier.of(AbyssOfIllusionMod.MOD_ID, id);
            RegistryKey<Recipe<?>> registryKey = RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(AbyssOfIllusionMod.MOD_ID, convertBetween(output, input)));
            this.exporter.accept(registryKey, new CampfireRecipe(i, o, d),
                    this.exporter.getAdvancementBuilder()
                            .criterion(hasItem(input), conditionsFromItem(input))
                            .build(d));
        }
    }

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
        return new ModRecipeGenerator(wrapperLookup, recipeExporter);
    }

    @Override
    public String getName() {
        return "Recipe";
    }
}
