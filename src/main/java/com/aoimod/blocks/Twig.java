package com.aoimod.blocks;

import net.fabricmc.loader.impl.game.minecraft.MinecraftGameProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;

public class Twig extends Block {
    static class TwigTypeEnum {
        public static final HashMap<String, TwigTypeEnum> record = new HashMap<>();

        String wood;

        TwigTypeEnum(String wood) {
            this.wood = wood;
        }

        static void build() {
            TagKey<Block> logs = TagKey.of(Registries.BLOCK.getKey(), Identifier.of("logs"));
            List<Block> blocks = Registries.BLOCK.streamEntries()
                    .filter(entry -> entry.isIn(logs))
                    .filter(entry -> {
                        String name = entry.value().toString();
                        return name.contains("log") && !name.contains("stripped");
                    })
                    .map(RegistryEntry.Reference::value)
                    .toList();

            for (var block: blocks) {
                record.put(block.getTranslationKey(), new TwigTypeEnum(block.getTranslationKey()));
            }

            System.out.println(record);
        }
    }

    public Twig(Settings settings) {
        super(settings);
    }
}
