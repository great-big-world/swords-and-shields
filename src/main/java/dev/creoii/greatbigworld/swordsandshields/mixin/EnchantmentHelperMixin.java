package dev.creoii.greatbigworld.swordsandshields.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
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
}
