package dev.creoii.greatbigworld.swordsandshields.mixin.component;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.element.Element;
import dev.creoii.greatbigworld.swordsandshields.util.ElementHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

@Mixin(ItemEnchantments.class)
public class ItemEnchantmentsComponentMixin {
    @WrapOperation(method = "addToTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;getFullname(Lnet/minecraft/core/Holder;I)Lnet/minecraft/network/chat/Component;"))
    private Component gbw$addElementSymbol(Holder<Enchantment> enchantment, int level, Operation<Component> original) {
        Component name = original.call(enchantment, level);
        Element element = ElementHolder.gbw$getElement(enchantment.unwrapKey().orElseThrow());
        if (element != null)
            return ComponentUtils.formatList(List.of(element.getSymbol(), name), Component.literal(" "));
        return name;
    }
}
