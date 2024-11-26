package com.aoimod.mixin;

import com.aoimod.customvalues.Thirsty;
import com.aoimod.networking.ServerMessages;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aoimod.customvalues.IThirsty;
import com.aoimod.networking.packet.ThirstyS2CPacket;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "finishUsing", at = @At("HEAD"))
    private void onFinishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.isOf(Items.POTION) && stack.get(DataComponentTypes.POTION_CONTENTS).matches(Potions.WATER)) {
            if (!world.isClient && user instanceof ServerPlayerEntity serverPlayer) {
                Thirsty thirsty = ((IThirsty) serverPlayer).getThirsty();
                float currentThirst = thirsty.getThirst();
                thirsty.addThirsty(4f);
                float newThirst = thirsty.getThirst();
                ServerMessages.syncThirstToClient(serverPlayer);
                serverPlayer.sendMessage(
                        Text.literal("§b[Debug] §f飲用藥水: ")
                                .append(String.format("§e口渴值 %.1f → %.1f", currentThirst, newThirst)),
                        false);
            }
        }
    }
}