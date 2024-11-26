package com.aoimod.networking.packet;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.blockentities.CampfireBlockEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record CampfireDataS2CPacket(CampfireBlockEntity.Packet inner) implements CustomPayload {
    public static final Id<CampfireDataS2CPacket> ID =
            new Id<>(Identifier.of(AbyssOfIllusionMod.MOD_ID, "campfire_packet"));
    public static final PacketCodec<RegistryByteBuf, CampfireDataS2CPacket> CODEC =
            PacketCodec.tuple(CampfireBlockEntity.PACKET_CODEC, CampfireDataS2CPacket::inner, CampfireDataS2CPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
