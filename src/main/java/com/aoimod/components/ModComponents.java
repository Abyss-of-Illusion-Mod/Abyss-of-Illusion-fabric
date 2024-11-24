package com.aoimod.components;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.blockentities.CampfireBlockEntity;
import com.aoimod.blocks.Campfire;
import com.aoimod.blocks.Twig;
import com.aoimod.networking.packet.CampfireDataS2CPacket;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.UnaryOperator;

public class ModComponents {
    public static ComponentType<Twig.TwigTypeEnum> TWIG_TYPE = register("twig_type", builder -> builder.codec(Twig.CODEC));

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builder) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(AbyssOfIllusionMod.MOD_ID, name),
                builder.apply(ComponentType.builder()).build());
    }

    public static void initialize() {

    }
}
