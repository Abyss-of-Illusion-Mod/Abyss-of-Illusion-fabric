package com.aoimod.items;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.blocks.CustomCobblestone;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {
    public static final Item STONE_SCRAPPER =
            register(StoneScrapper::new, new Item.Settings(), "stone_scrapper");
    public static final Item PEBBLE =
            register(Pebble::new, new Item.Settings(), "pebble");

    public static Item register(Function<Item.Settings, Item> factory, Item.Settings settings, String name) {
        Identifier id = Identifier.of(AbyssOfIllusionMod.MOD_ID, name);
        RegistryKey<Item> key = RegistryKey.of(Registries.ITEM.getKey(), id);
        return Items.register(key, factory, settings);
    }

    public static void initialize() {
        CustomCobblestone.initSpecial();
    }
}
