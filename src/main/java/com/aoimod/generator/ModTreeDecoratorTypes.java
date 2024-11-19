package com.aoimod.generator;

import com.aoimod.AbyssOfIllusionMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class ModTreeDecoratorTypes {
    public static final TreeDecoratorType<TwigTreeDecorator> TWIG_TREE_DECORATOR_TYPE =
            Registry.register(
                    Registries.TREE_DECORATOR_TYPE,
                    Identifier.of(AbyssOfIllusionMod.MOD_ID, "twig_tree_decorator"),
                    new TreeDecoratorType<>(TwigTreeDecorator.CODEC));
}
