package com.aoimod.recipes;

import com.aoimod.blockentities.CampfireBlockEntity;
import com.aoimod.recipes.serializer.CampfireRecipeSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public record CampfireRecipe(Ingredient input, ItemStack output, Identifier id) implements Recipe<CampfireBlockEntity> {
    public static class Type implements RecipeType<CampfireRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "campfire_recipe";

        private Type() {
        }
    }

    public CampfireRecipeSerializer.CampfireRecipeFormat.Output getOutput() {
        return new CampfireRecipeSerializer.CampfireRecipeFormat.Output(Registries.ITEM.getId(output.getItem()), output.getCount());
    }

    @Override
    public boolean matches(CampfireBlockEntity input, World world) {
        return this.input.test(input.getStackInSlot(0));
    }

    @Override
    public ItemStack craft(CampfireBlockEntity input, RegistryWrapper.WrapperLookup registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<? extends Recipe<CampfireBlockEntity>> getSerializer() {
        return CampfireRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<? extends Recipe<CampfireBlockEntity>> getType() {
        return Type.INSTANCE;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.forMultipleSlots(List.of(Optional.of(input)));
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return ModRecipeTypes.CAMPFIRE_BOOK_CATEGORY;
    }
}
