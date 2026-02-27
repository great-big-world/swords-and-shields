package dev.creoii.greatbigworld.swordsandshields.mixin.menu;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipePropertySet;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin extends ItemCombinerMenu {
    @Shadow @Final private RecipePropertySet baseItemTest;
    @Shadow @Final private RecipePropertySet additionItemTest;

    public SmithingMenuMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, ItemCombinerMenuSlotDefinition itemCombinerMenuSlotDefinition) {
        super(menuType, i, inventory, containerLevelAccess, itemCombinerMenuSlotDefinition);
    }

    @WrapOperation(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/SmithingMenu;shrinkStackInSlot(I)V", ordinal = 2))
    private void gbw$cancelClearSlot2(SmithingMenu instance, int i, Operation<Void> original) {
    }

    @WrapOperation(method = "getRelevantItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;getItem(I)Lnet/minecraft/world/item/ItemStack;", ordinal = 2))
    private ItemStack gbw$cancelSlot2Get1(Container instance, int i, Operation<ItemStack> original) {
        return ItemStack.EMPTY;
    }

    @WrapOperation(method = "createRecipeInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;getItem(I)Lnet/minecraft/world/item/ItemStack;", ordinal = 2))
    private ItemStack gbw$cancelSlot2Get2(Container instance, int i, Operation<ItemStack> original) {
        return ItemStack.EMPTY;
    }

    @WrapOperation(method = "slotsChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;hasItem()Z", ordinal = 2))
    private boolean gbw$cancelSlot2Get2(Slot instance, Operation<Boolean> original) {
        return true;
    }

    @Inject(method = "canMoveIntoInputSlots", at = @At("HEAD"), cancellable = true)
    private void gbw$fixCanMove(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(baseItemTest.test(itemStack) && !getSlot(1).hasItem() || additionItemTest.test(itemStack) && !getSlot(2).hasItem());
    }

    @WrapOperation(method = "createInputSlotDefinitions", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$Builder;withSlot(IIILjava/util/function/Predicate;)Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$Builder;", ordinal = 0))
    private static ItemCombinerMenuSlotDefinition.Builder gbw$adjustSlot0(ItemCombinerMenuSlotDefinition.Builder instance, int i, int j, int k, Predicate<ItemStack> predicate, Operation<ItemCombinerMenuSlotDefinition.Builder> original, @Local(ordinal = 0) RecipePropertySet recipePropertySet) {
        return instance.withSlot(i, 27, 47, recipePropertySet::test);
    }

    @WrapOperation(method = "createInputSlotDefinitions", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$Builder;withSlot(IIILjava/util/function/Predicate;)Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$Builder;", ordinal = 1))
    private static ItemCombinerMenuSlotDefinition.Builder gbw$adjustSlot1(ItemCombinerMenuSlotDefinition.Builder instance, int i, int j, int k, Predicate<ItemStack> predicate, Operation<ItemCombinerMenuSlotDefinition.Builder> original, @Local(ordinal = 2) RecipePropertySet recipePropertySet3) {
        return instance.withSlot(i, 76, 47, recipePropertySet3::test);
    }

    @WrapOperation(method = "createInputSlotDefinitions", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$Builder;withSlot(IIILjava/util/function/Predicate;)Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$Builder;", ordinal = 2))
    private static ItemCombinerMenuSlotDefinition.Builder gbw$removeSlot2(ItemCombinerMenuSlotDefinition.Builder instance, int i, int j, int k, Predicate<ItemStack> predicate, Operation<ItemCombinerMenuSlotDefinition.Builder> original) {
        return instance;
    }

    @WrapOperation(method = "createInputSlotDefinitions", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$Builder;withResultSlot(III)Lnet/minecraft/world/inventory/ItemCombinerMenuSlotDefinition$Builder;"))
    private static ItemCombinerMenuSlotDefinition.Builder gbw$adjustResultSlot(ItemCombinerMenuSlotDefinition.Builder instance, int i, int j, int k, Operation<ItemCombinerMenuSlotDefinition.Builder> original) {
        return instance.withResultSlot(i - 1, j + 36, k - 1);
    }
}
