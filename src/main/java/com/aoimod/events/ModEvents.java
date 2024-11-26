package com.aoimod.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class ModEvents {
    public static void initialize() {
        ServerTickEvents.START_SERVER_TICK.register(new PlayerTick());
    }
}
