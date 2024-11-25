package com.aoimod.networking.packet;

import com.aoimod.AbyssOfIllusionMod;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

// 玩家喝水發送的封包
public record ThirstyC2SPacket(float drinkValue) implements CustomPayload {
    public static final Id<ThirstyC2SPacket> ID = new Id<>(
            Identifier.of(AbyssOfIllusionMod.MOD_ID, "drinking_packet"));

    public static final PacketCodec<RegistryByteBuf, ThirstyC2SPacket> CODEC = PacketCodec.tuple(
        PacketCodecs.FLOAT,
        ThirstyC2SPacket::drinkValue,
        ThirstyC2SPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
