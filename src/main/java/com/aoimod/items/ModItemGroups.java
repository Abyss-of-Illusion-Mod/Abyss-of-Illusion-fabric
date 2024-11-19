package com.aoimod.items;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.blocks.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final RegistryKey<ItemGroup> NATURAL_GROUP_KEY =
            RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(AbyssOfIllusionMod.MOD_ID, "natural"));
    public static final ItemGroup NATURAL_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModBlocks.TWIG))
            .displayName(Text.translatable("itemGroup.abyss-of-illusion.natural"))
            .build();

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, NATURAL_GROUP_KEY, NATURAL_GROUP);
        ItemGroupEvents.modifyEntriesEvent(NATURAL_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(ModBlocks.TWIG.asItem());
        });
    }
}
