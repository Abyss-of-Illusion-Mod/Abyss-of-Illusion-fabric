package com.aoimod.blocks;

import com.aoimod.components.ModComponents;
import com.aoimod.properties.TwigProperty;
import com.aoimod.utils.Base37;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Twig extends Block implements Waterloggable {

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
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public Twig(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState().with(WATERLOGGED, false));
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
                .add(WATERLOGGED)
                .add(TWIG_TYPE)
                .add(FACING);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if (!world.isClient) {
            if (world.getBlockState(pos.offset(Direction.UP)).isOf(ModBlocks.TWIG)) {
                world.breakBlock(pos.offset(Direction.UP), true);
            }
        }

        super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        var bottomState = world.getBlockState(pos.offset(Direction.DOWN));
        return bottomState.isOpaqueFullCube() || !bottomState.isReplaceable();
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        ItemStack stack = new ItemStack(state.getBlock());
        Text displayName = Text.literal("")
                .append(Text.translatable(Base37.decode(state.get(TWIG_TYPE).toString())))
                .append(Text.translatable("block.abyss-of-illusion.twig"));
        stack.set(ModComponents.TWIG_TYPE, state.get(TWIG_TYPE));
        stack.set(DataComponentTypes.CUSTOM_NAME, displayName);
        stack.set(DataComponentTypes.ITEM_NAME, displayName);
        return List.of(stack);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        ItemStack stack = ctx.getStack();
        return this.getDefaultState()
                .with(WATERLOGGED, fluidState.isOf(Fluids.WATER))
                .with(FACING, ctx.getHorizontalPlayerFacing())
                .with(TWIG_TYPE, Objects.requireNonNullElse(
                        stack.get(ModComponents.TWIG_TYPE),
                        TwigTypeEnum.record.values().iterator().next()));
    }
}
