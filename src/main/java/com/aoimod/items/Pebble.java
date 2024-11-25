package com.aoimod.items;

import com.aoimod.blocks.Campfire;
import com.aoimod.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Pebble extends Item {
    public Pebble(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!world.isClient) {
            ItemStack stack = context.getStack();
            BlockPos pos = context.getBlockPos();
            BlockState targetState = world.getBlockState(pos);
            BlockState blockState = world.getBlockState(pos.offset(context.getSide()).down());
            if (targetState.getBlock() instanceof Campfire campfire) {
                ItemStack need = stack.split(campfire.getNeedPebble(targetState));
                if (need.getCount() > 0) {
                    world.setBlockState(pos, targetState.with(Campfire.PEBBLES, targetState.get(Campfire.PEBBLES) + need.getCount()));
                    world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.PLAYERS);
                }
            } else if (blockState.isOpaqueFullCube()) {
                pos = pos.offset(context.getSide());
                ItemStack need = stack.split(Campfire.NEED_PEBBLE_COUNT);
                world.setBlockState(pos, ModBlocks.CAMPFIRE.getDefaultState().with(Campfire.PEBBLES, need.getCount()));
                world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.PLAYERS);
            } else {
                return ActionResult.PASS;
            }

            return ActionResult.SUCCESS_SERVER;
        }

        return ActionResult.PASS;
    }
}
