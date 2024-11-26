package com.aoimod.blocks;

import com.aoimod.blockentities.CampfireBlockEntity;
import com.aoimod.blockentities.ModBlockEntityTypes;
import com.aoimod.items.ModItems;
import com.aoimod.networking.packet.CampfireDataS2CPacket;
import com.aoimod.recipes.CampfireRecipe;
import com.aoimod.utils.PlayerUtils;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class Campfire extends BlockWithEntity {
    public static final int NEED_PEBBLE_COUNT = 8;
    public static final int NEED_TWIG_COUNT = 4;
    public static final IntProperty PEBBLES = IntProperty.of("pebbles", 1, NEED_PEBBLE_COUNT);
    public static final IntProperty TWIGS = IntProperty.of("twigs", 0, NEED_TWIG_COUNT);
    public static final IntProperty FIRE_LEVEL = IntProperty.of("fire_level", 0, 3);

    public Campfire(Settings settings) {
        super(settings);
    }

    public int getNeedPebble(@NotNull BlockState state) {
        return NEED_PEBBLE_COUNT - state.get(PEBBLES);
    }

    public int getNeedTwig(@NotNull BlockState state) {
        return NEED_TWIG_COUNT - state.get(TWIGS);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(Campfire::new);
    }

    @Override
    protected void appendProperties(@NotNull StateManager.Builder<Block, BlockState> builder) {
        builder
                .add(PEBBLES)
                .add(TWIGS)
                .add(FIRE_LEVEL);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        return ActionResult.SUCCESS;
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity == null)
            return ActionResult.PASS;

        CampfireBlockEntity campfireBlockEntity = (CampfireBlockEntity) blockEntity;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        ServerWorld serverWorld = (ServerWorld) world;
        if (stack.isOf(ModBlocks.TWIG.asItem()) && state.get(TWIGS) < 4) {
            ItemStack used = stack.split(getNeedTwig(state));
            if (used.getCount() > 0) {
                campfireBlockEntity.addTwigs(used);

                int twigSize = (int) campfireBlockEntity.getTwigs().stream().filter(stack1 -> !stack1.isEmpty()).count();
                world.setBlockState(pos, state.with(TWIGS, twigSize));
                syncCampfireData(serverPlayer, serverWorld, campfireBlockEntity);
            }

            return ActionResult.SUCCESS;
        }

        BlockPos dropPos = pos.offset(hit.getSide());
        ServerRecipeManager manager = (ServerRecipeManager) world.getRecipeManager();
        ItemStack temp = campfireBlockEntity.setItem(stack);
        Optional<RecipeEntry<CampfireRecipe>> entry = manager.getFirstMatch(CampfireRecipe.Type.INSTANCE, campfireBlockEntity, world);
        if (entry.isPresent()) {
            ItemScatterer.spawn(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), campfireBlockEntity.setItem(stack));
            syncCampfireData(serverPlayer, serverWorld, campfireBlockEntity);
            return ActionResult.SUCCESS;
        } else {
            campfireBlockEntity.setItem(temp);
        }

        if (world.getFuelRegistry().isFuel(stack)) {
            ItemScatterer.spawn(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), campfireBlockEntity.setFuel(stack));
            syncCampfireData(serverPlayer, serverWorld, campfireBlockEntity);
            return ActionResult.SUCCESS;
        }

        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public BlockState onBreak(@NotNull World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (PlayerUtils.isSurvival(player) && blockEntity instanceof CampfireBlockEntity campfireBlockEntity) {
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
        return validateTicker(type, ModBlockEntityTypes.CAMPFIRE_BLOCK_ENTITY_TYPE, CampfireBlockEntity::tick);
    }

    public static void syncCampfireData(ServerPlayerEntity serverPlayer, ServerWorld serverWorld, CampfireBlockEntity campfireBlockEntity) {
        serverPlayer.server.execute(() -> serverWorld.getPlayers()
                .forEach(player1 -> ServerPlayNetworking.send(player1, new CampfireDataS2CPacket(campfireBlockEntity.createPacket()))));
    }
}
