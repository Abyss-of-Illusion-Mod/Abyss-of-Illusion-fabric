package com.aoimod.blocks;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.blockentities.CampfireBlockEntity;
import com.aoimod.blockentities.ModBlockEntityTypes;
import com.aoimod.items.ModItems;
import com.aoimod.networking.packet.CampfireDataS2CPacket;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Campfire extends Block implements BlockEntityProvider {
    public static final int NEED_PEBBLE_COUNT = 8;
    public static final int NEED_TWIG_COUNT = 4;
    public static final IntProperty PEBBLES = IntProperty.of("pebbles", 1, NEED_PEBBLE_COUNT);
    public static final IntProperty TWIGS = IntProperty.of("twigs", 0, NEED_TWIG_COUNT);

    public Campfire(Settings settings) {
        super(settings);
        UseBlockCallback.EVENT.register(
                Identifier.of(AbyssOfIllusionMod.MOD_ID, "use_twig"),
                (player, world, hand, hitResult) -> {
                    if (world.isClient)
                        return ActionResult.PASS;

                    BlockPos pos = hitResult.getBlockPos();
                    ItemStack stack = player.getStackInHand(hand);
                    if (!world.getBlockState(pos).isOf(ModBlocks.CAMPFIRE) ||
                        !stack.isOf(ModBlocks.TWIG.asItem()))
                        return ActionResult.PASS;

                    return withTwig((ServerPlayerEntity) player, world, hand, hitResult);
                });
    }

    public int getNeedPebble(@NotNull BlockState state) {
        return NEED_PEBBLE_COUNT - state.get(PEBBLES);
    }

    public int getNeedTwig(@NotNull BlockState state) {
        return NEED_TWIG_COUNT - state.get(TWIGS);
    }

    private ActionResult withTwig(@NotNull ServerPlayerEntity player, @NotNull World world, Hand hand, @NotNull BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = player.getStackInHand(hand);
        ItemStack used = stack.split(getNeedTwig(state));
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (used.getCount() > 0 && blockEntity instanceof CampfireBlockEntity campfireBlockEntity) {
            campfireBlockEntity.addTwigs(used);
            world.setBlockState(pos, state.with(TWIGS, campfireBlockEntity.getTwigs().size()));
            player.server.execute(() -> world.getPlayers().stream()
                    .filter(player1 -> player1 instanceof ServerPlayerEntity)
                    .map(player1 -> (ServerPlayerEntity) player1)
                    .forEach(player1 -> ServerPlayNetworking.send(player1, new CampfireDataS2CPacket(campfireBlockEntity.createPacket()))));
        }

        return ActionResult.SUCCESS;
    }

    @Override
    protected void appendProperties(@NotNull StateManager.Builder<Block, BlockState> builder) {
        builder
                .add(PEBBLES)
                .add(TWIGS);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CampfireBlockEntity campfireBlockEntity) {
            campfireBlockEntity.dropItems(world);
        }

        return super.onBreak(world, pos, state, player);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        return List.of(new ItemStack(ModItems.PEBBLE, state.get(PEBBLES)));
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Nullable
    @Override
    public CampfireBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CampfireBlockEntity(ModBlockEntityTypes.CAMPFIRE_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return BlockEntityProvider.super.getTicker(world, state, type);
    }
}