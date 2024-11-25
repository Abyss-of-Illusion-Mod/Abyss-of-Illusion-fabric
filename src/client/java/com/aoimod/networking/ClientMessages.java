package com.aoimod.networking;

import com.aoimod.blockentities.CampfireBlockEntity;
import com.aoimod.custonvalues.IThirsty;
import com.aoimod.networking.packet.CampfireDataS2CPacket;
import com.aoimod.networking.packet.ThirstyS2CPacket;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ClientMessages {
    public static void initialize() {
        PayloadTypeRegistry.playS2C().register(CampfireDataS2CPacket.ID, CampfireDataS2CPacket.CODEC);

        ClientPlayNetworking.registerGlobalReceiver(CampfireDataS2CPacket.ID, ClientMessages::campfireHandler);
        ClientPlayNetworking.registerGlobalReceiver(ThirstyS2CPacket.ID,  ClientMessages::thirstyHandler);
    }

    private static void thirstyHandler(ThirstyS2CPacket packet, ClientPlayNetworking.Context context) {
        context.client().execute(() -> {
            ((IThirsty)context.player()).getThirsty().setThirst(packet.thirstValue());
        });
    }

    private static void campfireHandler(CampfireDataS2CPacket packet, ClientPlayNetworking.Context context) {
        CampfireBlockEntity.Packet p = packet.packet();
        List<ItemStack> stacks = p.stacks();
        World world = context.player().getWorld();
        context.client().execute(() -> {
            if (world.getBlockEntity(p.pos()) instanceof CampfireBlockEntity campfire) {
                campfire.setStack(stacks);
                campfire.markDirty();
                campfire.updateState(world);
            }
        });
    }
}
