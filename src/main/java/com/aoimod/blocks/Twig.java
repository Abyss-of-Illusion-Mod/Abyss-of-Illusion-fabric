package com.aoimod.blocks;

import com.aoimod.generator.TwigTreeDecorator;
import com.aoimod.properties.TwigProperty;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Twig extends Block {

    public static class TwigTypeEnum implements Comparable<TwigTypeEnum> {
        public static final HashMap<String, TwigTypeEnum> record = new HashMap<>();

        String wood;

        TwigTypeEnum(String wood) {
            this.wood = wood.replace('_', '1').replace('.', '_');
        }

        public String toString() {
            return wood;
        }

        public boolean equals(Object other) {
            return other instanceof TwigTypeEnum && wood.equals(((TwigTypeEnum)other).wood);
        }

        @Override
        public int compareTo(@NotNull Twig.TwigTypeEnum o) {
            return wood.compareTo(o.wood);
        }

        static void build() {
            TagKey<Block> logs = TagKey.of(Registries.BLOCK.getKey(), Identifier.of("logs"));
            List<Block> blocks = Registries.BLOCK.streamEntries()
                    .filter(entry -> entry.isIn(logs))
                    .filter(entry -> {
                        String name = entry.value().toString();
                        return name.contains("log") && !name.contains("stripped");
                    })
                    .map(RegistryEntry.Reference::value)
                    .toList();

            for (var block: blocks) {
                System.out.println(block.getTranslationKey());
                record.put(block.getTranslationKey(), new TwigTypeEnum(block.getTranslationKey()));
            }

            System.out.println(record);
        }

        public static TwigTypeEnum getOrCreate(String wood) {
            return record.getOrDefault(wood, new TwigTypeEnum(wood));
        }

        public static TwigTypeEnum addEnum(String wood) {
            return record.put(wood, new TwigTypeEnum(wood));
        }

        static {
            addEnum("block.minecraft.oak_log");
            addEnum("block.minecraft.spruce_log");
            addEnum("block.minecraft.birch_log");
            addEnum("block.minecraft.jungle_log");
            addEnum("block.minecraft.acacia_log");
            addEnum("block.minecraft.cherry_log");
            addEnum("block.minecraft.dark_oak_log");
            addEnum("block.minecraft.mangrove_log");
        }
    }

    public static final TwigProperty WOOD_TYPE = new TwigProperty("wood_type", TwigTypeEnum.class);

    public Twig(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.125, 0, 0.25, 0.875, 0.125, 0.75);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WOOD_TYPE);
    }
}
