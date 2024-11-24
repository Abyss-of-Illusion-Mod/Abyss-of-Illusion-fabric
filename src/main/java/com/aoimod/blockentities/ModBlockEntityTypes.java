package com.aoimod.blockentities;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.blocks.Campfire;
import com.aoimod.blocks.ModBlocks;
import com.aoimod.items.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityType;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;

public class ModBlockEntityTypes {
    public static final BlockEntityType<CampfireBlockEntity> CAMPFIRE_BLOCK_ENTITY_TYPE =
            register("campfire_entity_type", FabricBlockEntityTypeBuilder.create(
                    ((Campfire)ModBlocks.CAMPFIRE)::createBlockEntity, ModBlocks.CAMPFIRE).build());

    public static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(AbyssOfIllusionMod.MOD_ID, name), blockEntityType);
    }

    public static void initialize() {

    }
}
