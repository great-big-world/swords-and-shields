package dev.creoii.greatbigworld.swordsandshields.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentUtil;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @WrapOperation(method = "method_60144", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;canBeCombined(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/registry/entry/RegistryEntry;)Z"))
    private static boolean gbw$modifyRemoveConflicts(RegistryEntry<Enchantment> first, RegistryEntry<Enchantment> second, Operation<Boolean> original) {
        return EnchantmentUtil.canBeCombined(first, second);
    }

    @WrapOperation(method = "isCompatible", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;canBeCombined(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/registry/entry/RegistryEntry;)Z"))
    private static boolean gbw$modifyIsCompatible(RegistryEntry<Enchantment> first, RegistryEntry<Enchantment> second, Operation<Boolean> original) {
        return EnchantmentUtil.canBeCombined(first, second);
    }

    @WrapOperation(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/ItemEnchantmentsComponent$Builder;build()Lnet/minecraft/component/type/ItemEnchantmentsComponent;"))
    private static ItemEnchantmentsComponent gbw$modifyAddEnchantments(ItemEnchantmentsComponent.Builder instance, Operation<ItemEnchantmentsComponent> original, @Local(argsOnly = true) ItemStack stack, @Local ItemEnchantmentsComponent itemEnchantmentsComponent) {
        int enchantmentPower = EnchantmentUtil.getEnchantmentPower(stack);
        int enchantmentLevelCap = EnchantmentUtil.getEnchantmentLevelCap(stack);

        if (enchantmentLevelCap <= 0)
            return original.call(instance);

        if (enchantmentPower >= enchantmentLevelCap)
            return itemEnchantmentsComponent;
        else {
            ItemEnchantmentsComponent.Builder override = new ItemEnchantmentsComponent.Builder(itemEnchantmentsComponent);
            for (RegistryEntry<Enchantment> entry : instance.getEnchantments()) {
                if (itemEnchantmentsComponent.getEnchantments().contains(entry))
                    continue;

                boolean full = false;

                int level = instance.getLevel(entry);
                if (level + enchantmentPower > enchantmentLevelCap) {
                    level = Math.min(level, enchantmentLevelCap - enchantmentPower);
                    full = true;
                }

                override.add(entry, level);

                if (full)
                    break;
            }

            return override.build();
        }
    }
}
