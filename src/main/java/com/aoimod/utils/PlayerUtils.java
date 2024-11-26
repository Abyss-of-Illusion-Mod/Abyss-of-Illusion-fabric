package com.aoimod.utils;

import net.minecraft.entity.player.PlayerEntity;

public class PlayerUtils {
    public static boolean isSurvival(PlayerEntity player) {
        return !player.isCreative() && !player.isSpectator();
    }
}
