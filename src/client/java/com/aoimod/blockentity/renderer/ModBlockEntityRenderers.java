package com.aoimod.blockentity.renderer;

import com.aoimod.blockentities.ModBlockEntityTypes;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class ModBlockEntityRenderers {
    public static void initialize() {
        BlockEntityRendererFactories.register(ModBlockEntityTypes.CAMPFIRE_BLOCK_ENTITY_TYPE, CampfireBlockEntityRenderer::new);
    }
}
