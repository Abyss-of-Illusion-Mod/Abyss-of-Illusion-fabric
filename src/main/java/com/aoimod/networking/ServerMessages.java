package com.aoimod.networking;

import com.aoimod.networking.packet.ThirstyC2SPacket;
import com.aoimod.networking.packet.ThirstyS2CPacket;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

import com.aoimod.custonvalues.IThirsty;

public class ServerMessages {
    public static void initialize() {
        // PayloadTypeRegistry.playC2S().register(id, codec);
        // ServerPlayNetworking.registerGlobalReceiver(id, handle);
        PayloadTypeRegistry.playC2S().register(ThirstyC2SPacket.ID, ThirstyC2SPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(ThirstyS2CPacket.ID, ThirstyS2CPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ThirstyC2SPacket.ID, ServerMessages::thirstyHandler);
    }

    private static void thirstyHandler(ThirstyC2SPacket packet, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        IThirsty thirsty = (IThirsty) player;
        thirsty.getThirsty().setThirst(packet.drinkValue());
        ServerPlayNetworking.send(player, new ThirstyS2CPacket(thirsty.getThirsty().getThirst()));
    }

    public static void syncThirstToClient(ServerPlayerEntity player) {
        float thirstValue = ((IThirsty) player).getThirsty().getThirst();
        ServerPlayNetworking.send(player, new ThirstyS2CPacket(thirstValue));
    }
}
