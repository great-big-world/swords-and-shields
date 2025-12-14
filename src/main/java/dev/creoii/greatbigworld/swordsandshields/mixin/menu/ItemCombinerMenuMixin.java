package dev.creoii.greatbigworld.swordsandshields.mixin.menu;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemCombinerMenu.class)
public class ItemCombinerMenuMixin {
    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ItemCombinerMenu;addStandardInventorySlots(Lnet/minecraft/world/Container;II)V"))
    private void gbw$offsetInventorySlotsForAnvil(ItemCombinerMenu instance, Container itemStacks, int i, int j, Operation<Void> original, @Local(argsOnly = true) MenuType<?> menuType) {
        if (menuType == MenuType.ANVIL) {
            j += 31;
        }

        original.call(instance, itemStacks, i, j);
    }
}
