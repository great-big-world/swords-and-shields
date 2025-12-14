package dev.creoii.greatbigworld.swordsandshields.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.swordsandshields.item.recipe.SmithingUpgradeRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class SwordsAndShieldsRecipes {
    public static RecipeSerializer<SmithingUpgradeRecipe> SMITHING_UPGRADE_RECIPE_SERIALIZER;

    public static void register() {
        SMITHING_UPGRADE_RECIPE_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "smithing_upgrade"), new SmithingUpgradeRecipe.Serializer());
    }
}
