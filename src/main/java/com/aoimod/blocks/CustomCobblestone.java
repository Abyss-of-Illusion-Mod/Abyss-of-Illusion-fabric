package com.aoimod.blocks;

import com.aoimod.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.context.ContextParameter;
import net.minecraft.util.context.ContextType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomCobblestone extends Block {
    public static final IntProperty TAKEN = IntProperty.of("taken", 0, 6);
    private static final ArrayList<ItemConvertible> SPECIAL = new ArrayList<>();

    private final Random random = new Random();

    public CustomCobblestone(Settings settings) {
        super(settings);
    }

    public static void initSpecial() {
        SPECIAL.add(Items.FLINT);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TAKEN);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            if (!player.isCreative() && state.get(TAKEN) != 6) {
                state = state.with(TAKEN, Math.min(6, state.get(TAKEN) + random.nextInt(1, 3)));
            }
        }

        return super.onBreak(world, pos, state, player);
    }

    @Override
    protected void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        if (state.get(TAKEN) != 6)
            world.setBlockState(pos, state);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        Vec3d vec = builder.get(LootContextParameters.ORIGIN);
        BlockPos center = new BlockPos(new Vec3i((int) Math.floor(vec.x), (int) Math.floor(vec.y), (int) Math.floor(vec.z)));
        Entity player = builder.get(LootContextParameters.THIS_ENTITY);
        ServerWorld world = builder.getWorld();

        if (player instanceof PlayerEntity) {
            center = center.offset(player.getFacing().getOpposite());

            DefaultedList<ItemStack> result = DefaultedList.of();
            result.add(new ItemStack(ModItems.PEBBLE));
            if (state.get(TAKEN) == 6) {
                int range = random.nextInt(10);
                if (range > 8) { // 10%
                    result.add(new ItemStack(SPECIAL.get(random.nextInt(SPECIAL.size()))));
                }
            }

            ItemScatterer.spawn(world, center, result);
        }

        return List.of();
    }
}
