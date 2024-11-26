package com.aoimod.recipes;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.recipes.serializer.CampfireRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipeTypes {
    public static void initialize() {
        Registry.register(Registries.RECIPE_TYPE, Identifier.of(AbyssOfIllusionMod.MOD_ID, CampfireRecipe.Type.ID), CampfireRecipe.Type.INSTANCE);
        Registry.register(Registries.RECIPE_SERIALIZER, CampfireRecipeSerializer.ID, CampfireRecipeSerializer.INSTANCE);
    }
}
