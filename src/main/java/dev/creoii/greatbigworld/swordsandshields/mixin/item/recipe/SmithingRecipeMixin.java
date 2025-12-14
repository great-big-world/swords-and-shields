package dev.creoii.greatbigworld.swordsandshields.mixin.item.recipe;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SmithingRecipe.class)
public interface SmithingRecipeMixin {
    @Shadow Ingredient baseIngredient();
    @Shadow Optional<Ingredient> additionIngredient();

    @Inject(method = "matches(Lnet/minecraft/world/item/crafting/SmithingRecipeInput;Lnet/minecraft/world/level/Level;)Z", at = @At("HEAD"), cancellable = true)
    private void gbw$fixSmithingRecipeMatch(SmithingRecipeInput smithingRecipeInput, Level level, CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof SmithingTrimRecipe) {
            cir.setReturnValue(false);
            return;
        }
        cir.setReturnValue(baseIngredient().test(smithingRecipeInput.template()) && Ingredient.testOptionalIngredient(additionIngredient(), smithingRecipeInput.base()));
    }
}
