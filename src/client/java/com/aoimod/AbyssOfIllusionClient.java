package com.aoimod;

import com.aoimod.blockentity.renderer.ModBlockEntityRenderers;
import com.aoimod.blocks.ModBlocks;
import com.aoimod.hud.ThirstHudOverlay;
import com.aoimod.networking.ClientMessages;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.RenderLayer;

public class AbyssOfIllusionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CAMPFIRE, RenderLayer.getTranslucent());

		ClientMessages.initialize();
		ModelLoadingPlugin.register(new AbyssOfIllusionModelLoadingPlugin());
		ModBlockEntityRenderers.initialize();
		HudRenderCallback.EVENT.register(new ThirstHudOverlay());
	}
}