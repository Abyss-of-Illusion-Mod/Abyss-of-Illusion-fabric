package com.aoimod;

import com.aoimod.blocks.CustomCobblestone;
import com.aoimod.blocks.ModBlocks;
import com.aoimod.components.ModComponents;
import com.aoimod.extra.ModTreeDecoratorTypes;
import com.aoimod.items.ModItemGroups;
import com.aoimod.items.ModItems;
import net.fabricmc.api.ModInitializer;

import net.minecraft.world.biome.BuiltinBiomes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbyssOfIllusionMod implements ModInitializer {
	public static final String MOD_ID = "abyss-of-illusion";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Mod...");
		ModItemGroups.initialize();
		ModTreeDecoratorTypes.initialize();
		ModComponents.initialize();
		ModBlocks.initialize();
		ModItems.initialize();
	}
}