package com.aoimod.blocks;

import com.aoimod.AbyssOfIllusionMod;
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
                    .burnable()
                    .nonOpaque(),
            "twig",
            true);
    public static final Block QUARTZITE = register(
            Quartzite::new,
            AbstractBlock.Settings.create()
                    .strength(2f)
                    .requiresTool(),
            "quartzite",
            true);
    public static final Block SHALE = register(
            Quartzite::new,
            AbstractBlock.Settings.create()
                    .strength(2f)
                    .requiresTool(),
            "shale",
            true);
    public static final Block CAMPFIRE = register(
            Campfire::new,
            AbstractBlock.Settings.create()
                    .strength(1.5f)
                    .nonOpaque(),
            "campfire",
            false);

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
    }
}
