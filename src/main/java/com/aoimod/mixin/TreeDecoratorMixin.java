package com.aoimod.mixin;

import com.aoimod.generator.TwigTreeDecorator;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.FeatureSize;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.root.RootPlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(TreeFeatureConfig.class)
public class TreeDecoratorMixin {
	@ModifyVariable(at = @At("HEAD"), method = "<init>", argsOnly = true, index = 8)
	private static List<TreeDecorator> redirectDecorators(
			List<TreeDecorator> decorators)
	{
		ArrayList<TreeDecorator> newDecorators = new ArrayList<>(decorators);
		newDecorators.add(new TwigTreeDecorator());
		return newDecorators;
	}
}