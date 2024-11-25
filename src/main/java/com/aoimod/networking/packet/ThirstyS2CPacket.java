package com.aoimod.networking.packet;

import com.aoimod.AbyssOfIllusionMod;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ThirstyS2CPacket(float thirstValue) implements CustomPayload {
    public static final Id<ThirstyS2CPacket> ID = new Id<>(
            Identifier.of(AbyssOfIllusionMod.MOD_ID, "thirsty_sync"));

    public static final PacketCodec<RegistryByteBuf, ThirstyS2CPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT,
            ThirstyS2CPacket::thirstValue,
            ThirstyS2CPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
