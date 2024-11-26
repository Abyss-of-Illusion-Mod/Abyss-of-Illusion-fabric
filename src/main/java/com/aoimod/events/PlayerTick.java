package com.aoimod.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Random;

import com.aoimod.customvalues.IThirsty;
import com.aoimod.networking.ServerMessages;

public class PlayerTick implements ServerTickEvents.StartTick {
    @Override
    public void onStartTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            if (new Random().nextFloat() < 0.01f) {
                ((IThirsty) player).getThirsty().removeThirst(1f);
                ServerMessages.syncThirstToClient(player);
                player.sendMessage(Text.literal("§b[Debug] §f口渴值 -1"), false);
            }
        }
    }
}
