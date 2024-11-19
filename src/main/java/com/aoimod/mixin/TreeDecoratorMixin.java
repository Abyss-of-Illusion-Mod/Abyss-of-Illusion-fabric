package com.aoimod.mixin;

import com.aoimod.blocks.Twig;
import com.aoimod.generator.TwigTreeDecorator;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.FeatureSize;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.root.RootPlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(TreeFeatureConfig.class)
public class TreeDecoratorMixin {
	@Mutable
	@Final
	@Shadow
	public List<TreeDecorator> decorators;

    public TreeDecoratorMixin(List<TreeDecorator> decorators) {
        this.decorators = decorators;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
	private void modifyDecorators(
			BlockStateProvider trunkProvider,
			TrunkPlacer trunkPlacer,
			BlockStateProvider foliageProvider,
			FoliagePlacer foliagePlacer,
			Optional<RootPlacer> rootPlacer,
			BlockStateProvider dirtProvider,
			FeatureSize minimumSize,
			List<TreeDecorator> decorators,
			boolean ignoreVines,
			boolean forceDirt,
			CallbackInfo info
	) {
		List<TreeDecorator> modifiedDecorators = new ArrayList<>(this.decorators);
		BlockState state = trunkProvider.get(Random.create(1337), new BlockPos(0, 0, 0));
		modifiedDecorators.add(new TwigTreeDecorator(Twig.TwigTypeEnum.getOrCreate(state.getBlock().getTranslationKey())));
		this.decorators = modifiedDecorators;
	}
}