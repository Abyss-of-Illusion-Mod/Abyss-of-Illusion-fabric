package com.aoimod.generator;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class TwigTreeDecorator extends TreeDecorator {
    public static final TwigTreeDecorator INSTANCE = new TwigTreeDecorator();
    public static final MapCodec<TwigTreeDecorator> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    protected TreeDecoratorType<?> getType() {
        return ModTreeDecoratorTypes.TWIG_TREE_DECORATOR_TYPE;
    }

    @Override
    public void generate(Generator generator) {
        System.out.println("called generator");
        generator.getLogPositions().forEach(blockPos -> {

        });
    }
}
