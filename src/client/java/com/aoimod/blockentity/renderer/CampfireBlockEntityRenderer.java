package com.aoimod.blockentity.renderer;

import com.aoimod.blockentities.CampfireBlockEntity;
import com.aoimod.blocks.Campfire;
import com.aoimod.items.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

import java.util.*;

public class CampfireBlockEntityRenderer implements BlockEntityRenderer<CampfireBlockEntity> {
    private static final ItemStack PEBBLE = new ItemStack(ModItems.PEBBLE);

    public CampfireBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {

    }

    @Override
    public void render(CampfireBlockEntity campfire, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        MinecraftClient client = MinecraftClient.getInstance();
        ItemRenderer itemRenderer = client.getItemRenderer();
        BlockRenderManager manager = client.getBlockRenderManager();
        BlockModelRenderer blockRenderer = manager.getModelRenderer();
        int renderLight = Arrays.stream(Direction.values())
                .map(d -> WorldRenderer.getLightmapCoordinates(Objects.requireNonNull(campfire.getWorld()), campfire.getPos().offset(d)))
                .max(Integer::compareTo)
                .orElse(light);

        float xSize = 0.5f - 9f / 32f;
        float ySize = 0.5f - 9f / 32f;
        float[] x = new float[] { 0f, xSize, xSize, xSize, 0, -xSize, -xSize, -xSize };
        float[] y = new float[] { 0f, 0f, -ySize, 2*-ySize, 2*-ySize, 2*-ySize, -ySize, 0f };

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
        matrices.translate(0.5f, -(9f / 32), 0);
        for (int i = 0; i<campfire.getCachedState().get(Campfire.PEBBLES); i++) {
            matrices.push();

            matrices.translate(x[i], y[i], 1f / 32);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-45 * i));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(4.5f));
            itemRenderer.renderItem(PEBBLE, ModelTransformationMode.GROUND, renderLight, overlay, matrices, vertexConsumers, campfire.getWorld(), 0);

            matrices.pop();
        }

        matrices.pop();
        matrices.push();

        List<ItemStack> twigs = campfire.getTwigs().stream().filter(stack -> !stack.isEmpty()).toList();
        ListIterator<ItemStack> iter = twigs.listIterator();
        float[] degrees = new float[] { 0, 180, 270, 180 };
        matrices.translate(0.5f, -0.36f, 0.5f);
        matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));
        while (iter.hasNext()) {
            int i = iter.nextIndex();
            ItemStack stack = iter.next();

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(degrees[i]));
            matrices.push();

            if (i > 1)
                matrices.translate(0, -0.06f, 0);
            matrices.translate(-0.05f, 0, 0);
            itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, renderLight, overlay, matrices, vertexConsumers, campfire.getWorld(), 0);

            matrices.pop();
        }

        matrices.pop();
    }
}
