package com.aoimod.mixin;

import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BuiltinBiomes.class)
public class BiomeMixin {
//    @Redirect(method = "bootstrap", at = @At(value = "INVOKE_ASSIGN", target = "createNormalForest"))
//    private static Biome createModifiedForest(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>>) {
//
//    }

}
