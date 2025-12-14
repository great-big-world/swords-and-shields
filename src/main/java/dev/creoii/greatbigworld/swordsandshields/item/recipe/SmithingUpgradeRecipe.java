package dev.creoii.greatbigworld.swordsandshields.item.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsDataComponentTypes;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsRecipes;
import dev.creoii.greatbigworld.swordsandshields.util.EquipmentUpgrading;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SmithingUpgradeRecipe implements SmithingRecipe {
    private final Ingredient base;
    private final Optional<Ingredient> addition;
    private @Nullable PlacementInfo placementInfo;

    public SmithingUpgradeRecipe(Ingredient base, Optional<Ingredient> addition) {
        this.base = base;
        this.addition = addition;
    }

    @Override
    public boolean matches(SmithingRecipeInput smithingRecipeInput, Level level) {
        if (EquipmentUpgrading.canUpgrade(smithingRecipeInput.base()))
            return SmithingRecipe.super.matches(smithingRecipeInput, level);
        return false;
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput recipeInput, HolderLookup.Provider provider) {
        ItemStack stack = EquipmentUpgrading.getUpgradeFor(recipeInput.base(), recipeInput.addition().getItem());
        if (stack.has(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES)) {
            int upgrade = stack.get(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES);
            stack.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES, upgrade + 1);
        }
        return stack;
    }

    @Override
    public RecipeSerializer<? extends SmithingRecipe> getSerializer() {
        return SwordsAndShieldsRecipes.SMITHING_UPGRADE_RECIPE_SERIALIZER;
    }

    @Override
    public PlacementInfo placementInfo() {
        if (placementInfo == null) {
            placementInfo = PlacementInfo.createFromOptionals(List.of(Optional.empty(), Optional.of(base), addition));
        }
        return placementInfo;
    }

    @Override
    public Optional<Ingredient> templateIngredient() {
        return Optional.empty();
    }

    @Override
    public Ingredient baseIngredient() {
        return base;
    }

    @Override
    public Optional<Ingredient> additionIngredient() {
        return addition;
    }

    public static class Serializer implements RecipeSerializer<SmithingUpgradeRecipe> {
        private static final MapCodec<SmithingUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> {
            return instance.group(
                    Ingredient.CODEC.fieldOf("base").forGetter(recipe -> recipe.base),
                    Ingredient.CODEC.fieldOf("addition").forGetter(recipe -> recipe.addition.orElseThrow())
            ).apply(instance, (base, addition) -> new SmithingUpgradeRecipe(base, Optional.of(addition)));
        });
        public static final StreamCodec<RegistryFriendlyByteBuf, SmithingUpgradeRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.base,
                Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, recipe -> recipe.addition,
                SmithingUpgradeRecipe::new);

        public MapCodec<SmithingUpgradeRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, SmithingUpgradeRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
