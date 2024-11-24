package com.aoimod;

import com.aoimod.blockentity.renderer.ModBlockEntityRenderers;
import com.aoimod.networking.ClientMessages;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.render.RenderLayer;

public class AbyssOfIllusionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientMessages.initialize();
		ModelLoadingPlugin.register(new AbyssOfIllusionModelLoadingPlugin());
		ModBlockEntityRenderers.initialize();
	}
}