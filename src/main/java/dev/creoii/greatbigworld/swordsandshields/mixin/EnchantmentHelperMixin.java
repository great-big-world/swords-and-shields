package dev.creoii.greatbigworld.swordsandshields.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @WrapOperation(method = "method_60144", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;areCompatible(Lnet/minecraft/core/Holder;Lnet/minecraft/core/Holder;)Z"))
    private static boolean gbw$modifyRemoveConflicts(Holder<Enchantment> first, Holder<Enchantment> second, Operation<Boolean> original) {
        return EnchantmentUtil.canBeCombined(first, second);
    }

    @WrapOperation(method = "isEnchantmentCompatible", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;areCompatible(Lnet/minecraft/core/Holder;Lnet/minecraft/core/Holder;)Z"))
    private static boolean gbw$modifyIsCompatible(Holder<Enchantment> first, Holder<Enchantment> second, Operation<Boolean> original) {
        return EnchantmentUtil.canBeCombined(first, second);
    }

    @WrapOperation(method = "updateEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/ItemEnchantments$Mutable;toImmutable()Lnet/minecraft/world/item/enchantment/ItemEnchantments;"))
    private static ItemEnchantments gbw$modifyAddEnchantments(ItemEnchantments.Mutable instance, Operation<ItemEnchantments> original, @Local(argsOnly = true) ItemStack stack, @Local ItemEnchantments itemEnchantmentsComponent) {
        int enchantmentPower = EnchantmentUtil.getEnchantmentPower(stack);
        int enchantmentLevelCap = EnchantmentUtil.getEnchantmentLevelCap(stack);

        if (enchantmentLevelCap <= 0)
            return original.call(instance);

        if (enchantmentPower >= enchantmentLevelCap)
            return itemEnchantmentsComponent;
        else {
            ItemEnchantments.Mutable override = new ItemEnchantments.Mutable(itemEnchantmentsComponent);
            for (Holder<Enchantment> entry : instance.keySet()) {
                if (itemEnchantmentsComponent.keySet().contains(entry))
                    continue;

                boolean full = false;

                int level = instance.getLevel(entry);
                if (level + enchantmentPower > enchantmentLevelCap) {
                    level = Math.min(level, enchantmentLevelCap - enchantmentPower);
                    full = true;
                }

                override.upgrade(entry, level);

                if (full)
                    break;
            }

            return override.toImmutable();
        }
    }
}
