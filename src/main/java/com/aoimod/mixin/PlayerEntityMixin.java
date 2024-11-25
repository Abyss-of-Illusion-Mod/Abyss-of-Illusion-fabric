package com.aoimod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aoimod.custonvalues.IThirsty;
import com.aoimod.custonvalues.Thirsty;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements IThirsty {
    private Thirsty thirsty;

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
    }
}


