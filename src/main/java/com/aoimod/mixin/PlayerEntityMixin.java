package com.aoimod.mixin;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.networking.packet.CampfireDataS2CPacket;
import com.aoimod.networking.packet.ThirstyS2CPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aoimod.customvalues.IThirsty;
import com.aoimod.customvalues.Thirsty;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements IThirsty {
    @Unique
    private Thirsty thirsty = null;

    @Override
    public Thirsty getThirsty() {
        if (thirsty == null) {
            thirsty = new Thirsty();
        }
        return thirsty;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    protected void injectWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        Thirsty thirsty = getThirsty();
        thirsty.writeNbt(nbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    protected void injectReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        Thirsty thirsty = getThirsty();
        thirsty.readNbt(nbt);
        ServerPlayConnectionEvents.JOIN.register(
                Identifier.of(AbyssOfIllusionMod.MOD_ID, "init_thirsty"),
                (handler, sender, server) -> server.execute(() ->
                        ServerPlayNetworking.send(handler.player, new ThirstyS2CPacket(getThirsty().getThirst()))));
    }
}


