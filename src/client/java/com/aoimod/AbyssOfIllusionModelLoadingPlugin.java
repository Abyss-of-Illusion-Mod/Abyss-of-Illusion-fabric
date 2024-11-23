package com.aoimod;

import com.aoimod.blockmodels.TwigInventoryModel;
import com.aoimod.blockmodels.TwigModel;
import com.aoimod.blocks.Twig;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.HashMap;

public class AbyssOfIllusionModelLoadingPlugin implements ModelLoadingPlugin {
    public static final HashMap<ModelIdentifier, UnbakedModel> modelTable = new HashMap<>();
    @Override
    public void initialize(Context pluginContext) {
        pluginContext.modifyModelOnLoad().register((original, context) -> {
            final ModelIdentifier id = context.topLevelId();
            if (id != null && modelTable.get(id) != null) {
                return modelTable.get(id);
            } else {
                return original;
            }
        });
    }

    static {
        for (var direction: Direction.values()) {
            for (var twig_type: Twig.TwigTypeEnum.record.values()) {
                for (var waterlogged: Properties.WATERLOGGED.getValues())
                    modelTable.put(new ModelIdentifier(Identifier.of(AbyssOfIllusionMod.MOD_ID, "twig"),
                            String.format("facing=%s,twig_type=%s,waterlogged=%b", direction, twig_type, waterlogged)),
                            new TwigModel(direction, twig_type));
            }

            modelTable.put(new ModelIdentifier(Identifier.of(AbyssOfIllusionMod.MOD_ID, "twig"), "inventory"), new TwigInventoryModel());
        }
    }
}
