package com.aoimod.blockmodels;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.blocks.Twig;
import com.aoimod.components.ModComponents;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class TwigInventoryModel implements UnbakedModel, BakedModel, FabricBakedModel {
    private static final Transformation THIRD_HAND =
            new Transformation(new Vector3f(75f, 45f, 0f), new Vector3f(0f, 0.2f, 0.4f), new Vector3f(0.75f, 0.75f, 0.75f));
    private static final Transformation FIRST_HAND =
            new Transformation(new Vector3f(), new Vector3f(0f, 0.5f, 0.1f), new Vector3f(0.75f, 0.75f, 0.75f));
    private static final Transformation GUI =
            new Transformation(new Vector3f(45f, 45f, 0f), new Vector3f(0.125f, 0.125f, 0f), new Vector3f(0.9f, 0.9f, 0.9f));
    private static final Transformation GROUND =
            new Transformation(new Vector3f(), new Vector3f(0f, 0.25f, 0f), new Vector3f(0.6f, 0.6f, 0.6f));
    private static final Transformation FIXED =
            new Transformation(new Vector3f(), new Vector3f(), new Vector3f(1.2f, 1.2f, 1.2f));
    private static final ModelTransformation SCALED = new ModelTransformation(
            THIRD_HAND, THIRD_HAND,
            FIRST_HAND, FIRST_HAND,
            Transformation.IDENTITY, GUI,
            GROUND, FIXED
    );

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return null;
    }

    @Override
    public ModelTransformation getTransformation() {
        return SCALED;
    }

    @Override
    public void resolve(Resolver resolver) {

    }

    @Override
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer) {
        return this;
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        Twig.TwigTypeEnum twigType = stack.getComponents().get(ModComponents.TWIG_TYPE);
        if (twigType == null)
            twigType = Twig.TwigTypeEnum.record.values().iterator().next();

        ModelIdentifier mid = new ModelIdentifier(
                Identifier.of(AbyssOfIllusionMod.MOD_ID, "twig"),
                String.format("facing=%s,twig_type=%s", Direction.NORTH, twigType));
        BakedModel variantModel = MinecraftClient.getInstance().getBakedModelManager().getModel(mid);
        if (variantModel != null) {
            variantModel.emitItemQuads(stack, randomSupplier, context);
        }
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }
}
