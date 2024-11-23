package com.aoimod.items;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.blocks.ModBlocks;
import com.aoimod.blocks.Twig;
import com.aoimod.components.ModComponents;
import com.aoimod.utils.Base37;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;

public class ModItemGroups {
    public static final RegistryKey<ItemGroup> NATURAL_GROUP_KEY =
            RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(AbyssOfIllusionMod.MOD_ID, "natural"));
    public static final RegistryKey<ItemGroup> TOOLS_GROUP_KEY =
            RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(AbyssOfIllusionMod.MOD_ID, "tools"));

    public static final ItemGroup NATURAL_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModBlocks.TWIG))
            .displayName(Text.translatable("itemGroup.abyss-of-illusion.natural"))
            .build();
    public static final ItemGroup TOOLS_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.STONE_SCRAPPER))
            .displayName(Text.translatable("itemGroup.abyss-of-illusion.tools"))
            .build();

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, NATURAL_GROUP_KEY, NATURAL_GROUP);
        Registry.register(Registries.ITEM_GROUP, TOOLS_GROUP_KEY, TOOLS_GROUP);

        ItemGroupEvents.modifyEntriesEvent(NATURAL_GROUP_KEY).register(itemGroup -> {
            for (var twigType: Twig.TwigTypeEnum.record.values()) {
                ItemStack stack = new ItemStack(ModBlocks.TWIG.asItem());
                Text displayName = Text.literal("")
                        .append(Text.translatable(Base37.decode(twigType.toString())))
                        .append(Text.translatable("block.abyss-of-illusion.twig"));
                stack.set(ModComponents.TWIG_TYPE, twigType);
                stack.set(DataComponentTypes.CUSTOM_NAME, displayName);
                stack.set(DataComponentTypes.ITEM_NAME, displayName);
                itemGroup.add(stack);
            }

            itemGroup.add(ModItems.PEBBLE);
            itemGroup.add(ModBlocks.QUARTZITE);
            itemGroup.add(ModBlocks.SHALE);
        });

        ItemGroupEvents.modifyEntriesEvent(TOOLS_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(ModItems.STONE_SCRAPPER);
        });
    }
}
