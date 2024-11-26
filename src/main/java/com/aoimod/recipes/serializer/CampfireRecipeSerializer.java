package com.aoimod.recipes.serializer;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.recipes.CampfireRecipe;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class CampfireRecipeSerializer implements RecipeSerializer<CampfireRecipe> {
    public record CampfireRecipeFormat(Ingredient input, CampfireRecipeSerializer.CampfireRecipeFormat.Output output,
                                       Identifier recipe) {
            public record Output(Identifier id, int count) {
                public static final MapCodec<Output> CODEC =
                        RecordCodecBuilder.mapCodec(instance -> instance.group(
                                Identifier.CODEC.fieldOf("id").forGetter(Output::id),
                                Codec.INT.fieldOf("count").forGetter(Output::count)
                        ).apply(instance, Output::new));

                public ItemStack getItemStack() {
                    return new ItemStack(Registries.ITEM.getOptionalValue(id).orElse(ItemStack.EMPTY.getItem()), count);
                }
            }

            public static final MapCodec<CampfireRecipeFormat> CODEC =
                    RecordCodecBuilder.mapCodec(instance -> instance.group(
                            Ingredient.CODEC.fieldOf("input").forGetter(CampfireRecipeFormat::input),
                            Output.CODEC.fieldOf("output").forGetter(CampfireRecipeFormat::output),
                            Identifier.CODEC.fieldOf("recipe_id").forGetter(CampfireRecipeFormat::recipe)
                    ).apply(instance, CampfireRecipeFormat::new));
    }

    public static final MapCodec<CampfireRecipe> CODEC =
            CampfireRecipeFormat.CODEC.xmap(
                    format -> new CampfireRecipe(format.input, format.output.getItemStack(), format.recipe),
                    recipe -> new CampfireRecipeFormat(recipe.input(), recipe.getOutput(), recipe.id()));

    public static final PacketCodec<RegistryByteBuf, CampfireRecipe> PACKET_CODEC =
            PacketCodec.tuple(
                    Ingredient.PACKET_CODEC, CampfireRecipe::input,
                    ItemStack.PACKET_CODEC, CampfireRecipe::output,
                    Identifier.PACKET_CODEC, CampfireRecipe::id,
                    CampfireRecipe::new);

    public static final CampfireRecipeSerializer INSTANCE = new CampfireRecipeSerializer();
    public static final Identifier ID = Identifier.of(AbyssOfIllusionMod.MOD_ID, CampfireRecipe.Type.ID);

    private CampfireRecipeSerializer() {}

    @Override
    public MapCodec<CampfireRecipe> codec() {
        return CODEC;
    }

    @Override
    public PacketCodec<RegistryByteBuf, CampfireRecipe> packetCodec() {
        return PACKET_CODEC;
    }
}
