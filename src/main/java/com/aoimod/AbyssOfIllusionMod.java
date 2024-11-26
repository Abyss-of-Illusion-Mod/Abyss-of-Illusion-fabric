package com.aoimod;

import com.aoimod.blockentities.ModBlockEntityTypes;
import com.aoimod.blocks.CustomCobblestone;
import com.aoimod.blocks.ModBlocks;
import com.aoimod.components.ModComponents;
import com.aoimod.event.PlayerTick;
import com.aoimod.extra.ModTreeDecoratorTypes;
import com.aoimod.items.ModItemGroups;
import com.aoimod.items.ModItems;
import com.aoimod.recipes.ModRecipeTypes;
import com.aoimod.networking.ServerMessages;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.biome.BuiltinBiomes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbyssOfIllusionMod implements ModInitializer {
	public static final String MOD_ID = "abyss-of-illusion";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Mod...");
		ServerMessages.initialize();
		ModTreeDecoratorTypes.initialize();
		ModBlockEntityTypes.initialize();
		ModBlocks.initialize();
		ModComponents.initialize();
		ModItemGroups.initialize();
		ModItems.initialize();
		ModRecipeTypes.initialize();

		ServerTickEvents.START_SERVER_TICK.register(new PlayerTick());
	}
}