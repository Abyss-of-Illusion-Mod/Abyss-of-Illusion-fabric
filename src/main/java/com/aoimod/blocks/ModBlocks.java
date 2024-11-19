package com.aoimod.blocks;

import com.aoimod.AbyssOfIllusionMod;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModBlocks {
    public static final Block TWIG = register(
            Twig::new,
            AbstractBlock.Settings.create()
                    .burnable(),
            "twig",
            true);
    public static Block register(Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings, String name, boolean forItem) {
        Identifier id = Identifier.of(AbyssOfIllusionMod.MOD_ID, name);
        RegistryKey<Block> key = RegistryKey.of(Registries.BLOCK.getKey(), id);
        Block block = Blocks.register(key, factory, settings);
        if (forItem) {
            Items.register(block);
        }

        return block;
    }

    public static void initialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(minecraftServer -> {
            Twig.TwigTypeEnum.build();
        });
    }
}
