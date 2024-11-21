package com.aoimod.blockmodels;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.AbyssOfIllusionModelLoadingPlugin;
import com.aoimod.blocks.Twig;
import com.aoimod.components.ModComponents;
import com.aoimod.utils.Base37;
import com.aoimod.utils.Rotate;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class TwigModel implements UnbakedModel, BakedModel, FabricBakedModel {
    private static final HashMap<String, Pair<SpriteIdentifier, SpriteIdentifier>> SPRITE_IDS = new HashMap<>();
    private static final HashMap<String, Pair<Sprite, Sprite>> SPRITES = new HashMap<>();
    private Mesh mesh;
    private final Direction direction;
    private final Twig.TwigTypeEnum type;

    public TwigModel(Direction direction, Twig.TwigTypeEnum type) {
        this.direction = direction;
        this.type = type;
    }

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
        return true;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return SPRITES.get(type.toString()).getLeft();
    }

    @Override
    public ModelTransformation getTransformation() {
        return ModelHelper.MODEL_TRANSFORM_BLOCK;
    }

    @Override
    public void resolve(Resolver resolver) {

    }

    @Override
    public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer) {
        var key = type.toString();
        if (SPRITES.get(key) == null) {
            var p = SPRITE_IDS.get(key);
            SPRITES.put(key, new Pair<>(textureGetter.apply(p.getLeft()), textureGetter.apply(p.getRight())));
        }

        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        assert renderer != null;

        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();
        Sprite side = SPRITES.get(key).getLeft();
        Sprite top = SPRITES.get(key).getRight();
        float first = 22.5f, second = -22.5f;
        float angle = switch (direction) {
            case SOUTH -> 270;
            case WEST -> 180;
            case NORTH -> 90;
            default -> 0;
        };

        emitter.pos(0, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(10f / 16, 1f / 16, 8f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(1, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(10f / 16, 0f / 16, 8f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(2, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(14f / 16, 0f / 16, 8f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(3, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(14f / 16, 1f / 16, 8f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.uv(0, new Vector2f(0f, 16f));
        emitter.uv(1, new Vector2f(0f, 0f));
        emitter.uv(2, new Vector2f(4f, 0f));
        emitter.uv(3, new Vector2f(4f, 16f));
        emitter.spriteBake(side, MutableQuadView.BAKE_ROTATE_90);
        emitter.color(-1, -1, -1, -1);
        emitter.emit();

        emitter.pos(0, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(9f / 16, 1f / 16, 7f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(1, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(10f / 16, 1f / 16, 8f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(2, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(14f / 16, 1f / 16, 8f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(3, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(14f / 16, 1f / 16, 7f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.uv(0, new Vector2f(11f, 16f));
        emitter.uv(1, new Vector2f(12f, 0f));
        emitter.uv(2, new Vector2f(16f, 0f));
        emitter.uv(3, new Vector2f(16f, 16f));
        emitter.spriteBake(side, MutableQuadView.BAKE_ROTATE_90);
        emitter.color(-1, -1, -1, -1);
        emitter.emit();

        emitter.pos(0, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(9f / 16, 0f / 16, 7f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(1, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(9f / 16, 1f / 16, 7f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(2, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(14f / 16, 1f / 16, 7f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(3, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(14f / 16, 0f / 16, 7f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.uv(0, new Vector2f(0f, 16f));
        emitter.uv(1, new Vector2f(0f, 0f));
        emitter.uv(2, new Vector2f(5f, 0f));
        emitter.uv(3, new Vector2f(5f, 16f));
        emitter.spriteBake(side, MutableQuadView.BAKE_ROTATE_90);
        emitter.color(-1, -1, -1, -1);
        emitter.emit();

        emitter.pos(0, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(9f / 16, 0f / 16, 8f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(1, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(9f / 16, 0f / 16, 7f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(2, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(14f / 16, 0f / 16, 7f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(3, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(14f / 16, 0f / 16, 8f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.uv(0, new Vector2f(0f, 16f));
        emitter.uv(1, new Vector2f(0f, 0f));
        emitter.uv(2, new Vector2f(5f, 0f));
        emitter.uv(3, new Vector2f(5f, 16f));
        emitter.spriteBake(side, MutableQuadView.BAKE_ROTATE_90);
        emitter.color(-1, -1, -1, -1);
        emitter.emit();

        emitter.pos(0, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(14f / 16, 1f / 16, 8f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(1, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(14f / 16, 0f / 16, 8f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(2, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(14f / 16, 0f / 16, 7f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(3, Rotate.rotate(angle, Rotate.rotate(first, new Vector3f(14f / 16, 1f / 16, 7f / 16), 10f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.uv(0, new Vector2f(0f, 16f));
        emitter.uv(1, new Vector2f(0f, 0f));
        emitter.uv(2, new Vector2f(16f, 0f));
        emitter.uv(3, new Vector2f(16f, 16f));
        emitter.spriteBake(top, 0);
        emitter.color(-1, -1, -1, -1);
        emitter.emit();

        emitter.pos(0, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(3f / 16, 1f / 16, 8f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(1, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(3f / 16, 0f / 16, 8f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(2, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(14f / 16, 0f / 16, 8f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(3, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(14f / 16, 1f / 16, 8f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.uv(0, new Vector2f(0f, 16f));
        emitter.uv(1, new Vector2f(0f, 0f));
        emitter.uv(2, new Vector2f(11f, 0f));
        emitter.uv(3, new Vector2f(11f, 16f));
        emitter.spriteBake(side, MutableQuadView.BAKE_ROTATE_90);
        emitter.color(-1, -1, -1, -1);
        emitter.emit();

        emitter.pos(0, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(3f / 16, 1f / 16, 7f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(1, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(3f / 16, 1f / 16, 8f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(2, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(14f / 16, 1f / 16, 8f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(3, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(14f / 16, 1f / 16, 7f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.uv(0, new Vector2f(0f, 16f));
        emitter.uv(1, new Vector2f(0f, 0f));
        emitter.uv(2, new Vector2f(11f, 0f));
        emitter.uv(3, new Vector2f(11f, 16f));
        emitter.spriteBake(side, MutableQuadView.BAKE_ROTATE_90);
        emitter.color(-1, -1, -1, -1);
        emitter.emit();

        emitter.pos(0, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(3f / 16, 0f / 16, 7f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(1, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(3f / 16, 1f / 16, 7f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(2, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(14f / 16, 1f / 16, 7f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(3, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(14f / 16, 0f / 16, 7f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.uv(0, new Vector2f(0f, 16f));
        emitter.uv(1, new Vector2f(0f, 0f));
        emitter.uv(2, new Vector2f(11f, 0f));
        emitter.uv(3, new Vector2f(11f, 16f));
        emitter.spriteBake(side, MutableQuadView.BAKE_ROTATE_90);
        emitter.color(-1, -1, -1, -1);
        emitter.emit();

        emitter.pos(1, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(3f / 16, 0f / 16, 7f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(0, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(3f / 16, 0f / 16, 8f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(3, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(14f / 16, 0f / 16, 8f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(2, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(14f / 16, 0f / 16, 7f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.uv(0, new Vector2f(0f, 16f));
        emitter.uv(1, new Vector2f(0f, 0f));
        emitter.uv(2, new Vector2f(11f, 0f));
        emitter.uv(3, new Vector2f(11f, 16f));
        emitter.spriteBake(side, MutableQuadView.BAKE_ROTATE_90);
        emitter.color(-1, -1, -1, -1);
        emitter.emit();

        emitter.pos(1, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(14f / 16, 1f / 16, 7f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(0, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(14f / 16, 0f / 16, 7f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(3, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(14f / 16, 0f / 16, 8f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(2, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(14f / 16, 1f / 16, 8f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.uv(0, new Vector2f(0f, 16f));
        emitter.uv(1, new Vector2f(0f, 0f));
        emitter.uv(2, new Vector2f(16f, 0f));
        emitter.uv(3, new Vector2f(16f, 16f));
        emitter.spriteBake(top, 0);
        emitter.color(-1, -1, -1, -1);
        emitter.emit();

        emitter.pos(1, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(3f / 16, 1f / 16, 8f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(0, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(3f / 16, 0f / 16, 8f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(3, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(3f / 16, 0f / 16, 7f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.pos(2, Rotate.rotate(angle, Rotate.rotate(second, new Vector3f(3f / 16, 1f / 16, 7f / 16), 8.5f / 16, 0.5f / 16, 7.5f / 16, 0f, 1f, 0f), 0.5f, 0f, 0.5f, 0f, 1f, 0f));
        emitter.uv(0, new Vector2f(0f, 16f));
        emitter.uv(1, new Vector2f(0f, 0f));
        emitter.uv(2, new Vector2f(16f, 0f));
        emitter.uv(3, new Vector2f(16f, 16f));
        emitter.spriteBake(top, 0);
        emitter.color(-1, -1, -1, -1);
        emitter.emit();

        mesh = builder.build();
        return this;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        mesh.outputTo(context.getEmitter());
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        mesh.outputTo(context.getEmitter());
    }

    static {
        Collection<Twig.TwigTypeEnum> types = Twig.TwigTypeEnum.record.values();
        for (var type : types) {
            String[] translateKeyParts = Base37.decode(type.toString()).split("\\.");
            Identifier sideId = Identifier.of(translateKeyParts[1], "block/" + translateKeyParts[2]);
            Identifier topId = Identifier.of(translateKeyParts[1], "block/" + translateKeyParts[2] + "_top");
            Pair<SpriteIdentifier, SpriteIdentifier> p = new Pair<>(new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, sideId), new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, topId));
            SPRITE_IDS.put(type.toString(), p);
        }
    }
}
