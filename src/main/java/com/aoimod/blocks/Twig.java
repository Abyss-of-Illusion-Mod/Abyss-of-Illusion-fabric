package com.aoimod.blocks;

import com.aoimod.components.ModComponents;
import com.aoimod.properties.TwigProperty;
import com.aoimod.utils.Base37;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Twig extends Block {

    public static class TwigTypeEnum implements Comparable<TwigTypeEnum> {
        public static final HashMap<String, TwigTypeEnum> record = new HashMap<>();

        String wood;

        TwigTypeEnum(String wood) {
            this.wood = wood;
        }

        public String toString() {
            return wood;
        }

        public boolean equals(Object other) {
            return other instanceof TwigTypeEnum && wood.equals(((TwigTypeEnum)other).wood);
        }

        @Override
        public int compareTo(@NotNull Twig.TwigTypeEnum o) {
            return wood.compareTo(o.wood);
        }

        public static TwigTypeEnum getOrCreate(String wood) {
            String encoded = Base37.encode(wood);
            return record.getOrDefault(encoded, new TwigTypeEnum(encoded));
        }

        public static TwigTypeEnum getRaw(String encoded) {
            return record.get(encoded);
        }

        public static TwigTypeEnum addEnum(String wood) {
            String encoded = Base37.encode(wood);
            return record.put(encoded, new TwigTypeEnum(encoded));
        }

        static {
            addEnum("block.minecraft.oak_log");
            addEnum("block.minecraft.spruce_log");
            addEnum("block.minecraft.birch_log");
            addEnum("block.minecraft.jungle_log");
            addEnum("block.minecraft.acacia_log");
            addEnum("block.minecraft.cherry_log");
            addEnum("block.minecraft.dark_oak_log");
            addEnum("block.minecraft.mangrove_log");
        }
    }

    public static final Codec<TwigTypeEnum> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("twig_type")
                            .forGetter(TwigTypeEnum::toString)
            ).apply(instance, TwigTypeEnum::getRaw));
    public static final TwigProperty TWIG_TYPE = new TwigProperty("twig_type", TwigTypeEnum.class);
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;

    public Twig(Settings settings) {
        super(settings);
    }

    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0, 0, 0, 1, 0.125, 1);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder
                .add(TWIG_TYPE)
                .add(FACING);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        ItemStack stack = new ItemStack(state.getBlock());
        stack.set(ModComponents.TWIG_TYPE, state.get(TWIG_TYPE));
        return List.of(stack);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        ItemStack stack = ctx.getStack();
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing())
                .with(TWIG_TYPE, Objects.requireNonNullElse(
                        stack.get(ModComponents.TWIG_TYPE),
                        TwigTypeEnum.record.values().iterator().next()));
    }
}
