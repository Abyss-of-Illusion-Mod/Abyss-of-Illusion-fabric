package com.aoimod.mixin;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aoimod.custonvalues.IThirsty;
import com.aoimod.networking.packet.ThirstyS2CPacket;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "finishUsing", at = @At("HEAD"))
    private void onFinishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.isOf(Items.POTION)) {
            if (!world.isClient && user instanceof ServerPlayerEntity serverplayer) {
                float currentThirst = ((IThirsty) serverplayer).getThirsty().getThirst();
                float newThirst = Math.min(currentThirst + 4f, 20.0f);
                ((IThirsty) serverplayer).getThirsty().addThirsty(4f);
                ServerPlayNetworking.send(serverplayer, new ThirstyS2CPacket(4f));
                serverplayer.sendMessage(
                        Text.literal("§b[Debug] §f飲用藥水: ")
                                .append(String.format("§e口渴值 %.1f → %.1f", currentThirst, newThirst)),
                        false);
            }
        }
    }
}