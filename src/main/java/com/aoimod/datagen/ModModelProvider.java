package com.aoimod.datagen;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.blocks.CustomCobblestone;
import com.aoimod.blocks.ModBlocks;
import com.aoimod.items.ModItems;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataOutput;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerParentedItemModel(ModBlocks.TWIG, Identifier.of(AbyssOfIllusionMod.MOD_ID, "block/twig"));
        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier
                        .create(ModBlocks.TWIG)
                        .coordinate(BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
                                .register(Direction.EAST,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.MODEL, Identifier.of(AbyssOfIllusionMod.MOD_ID, "block/twig")))
                                .register(Direction.NORTH,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
                                                .put(VariantSettings.MODEL, Identifier.of(AbyssOfIllusionMod.MOD_ID, "block/twig")))
                                .register(Direction.WEST,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R180)
                                                .put(VariantSettings.MODEL, Identifier.of(AbyssOfIllusionMod.MOD_ID, "block/twig")))
                                .register(Direction.SOUTH,
                                        BlockStateVariant.create()
                                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                                                .put(VariantSettings.MODEL, Identifier.of(AbyssOfIllusionMod.MOD_ID, "block/twig")))));

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.QUARTZITE);
        blockStateModelGenerator.registerParentedItemModel(ModBlocks.QUARTZITE, Identifier.of(AbyssOfIllusionMod.MOD_ID, "block/quartzite"));
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.SHALE);
        blockStateModelGenerator.registerParentedItemModel(ModBlocks.SHALE, Identifier.of(AbyssOfIllusionMod.MOD_ID, "block/shale"));
        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(Blocks.COBBLESTONE)
                        .coordinate(BlockStateVariantMap.create(CustomCobblestone.TAKEN)
                                .register(0, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, Identifier.of("block/cobblestone")))
                                .register(1, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, Identifier.of(AbyssOfIllusionMod.MOD_ID, "block/cobblestone-1")))
                                .register(2, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, Identifier.of(AbyssOfIllusionMod.MOD_ID, "block/cobblestone-2")))
                                .register(3, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, Identifier.of(AbyssOfIllusionMod.MOD_ID, "block/cobblestone-3")))
                                .register(4, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, Identifier.of(AbyssOfIllusionMod.MOD_ID, "block/cobblestone-4")))
                                .register(5, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, Identifier.of(AbyssOfIllusionMod.MOD_ID, "block/cobblestone-5")))
                                .register(6, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, Identifier.of("block/cobblestone")))));

        for (int i=1; i<=5; i++) {
            Identifier id = Identifier.of(AbyssOfIllusionMod.MOD_ID, String.format("block/cobblestone-%d", i));
            blockStateModelGenerator.modelCollector.accept(
                    id, () -> {
                        var element = new SimpleModelSupplier(Identifier.of("block/cobblestone")).get()
                                .getAsJsonObject();
                        var textures = new JsonObject();
                        textures.addProperty("all", id.toString());
                        element.add("textures", textures);
                        return element;
                    });
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.PEBBLE, Models.GENERATED);
        itemModelGenerator.register(ModItems.STONE_SCRAPPER, Models.GENERATED);
    }
}
