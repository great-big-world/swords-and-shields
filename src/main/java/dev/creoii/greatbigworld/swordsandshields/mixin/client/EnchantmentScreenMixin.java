package dev.creoii.greatbigworld.swordsandshields.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.client.GreatBigWorldClient;
import dev.creoii.greatbigworld.element.Element;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.swordsandshields.util.ElementHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.enchantment.Enchantment;

@Mixin(EnchantmentScreen.class)
public class EnchantmentScreenMixin {
    @WrapOperation(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/EnchantmentMenu;getGoldCount()I"))
    private int gbw$ignoreLapisCount(EnchantmentMenu instance, Operation<Integer> original) {
        Set<Knowledge> knowledge = GreatBigWorldClient.getKnowledge().getOrDefault(Knowledge.Type.ENCHANTMENT, null);

        if (knowledge == null)
            return 0;

        return Math.min(3, knowledge.size());
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;getFullname(Lnet/minecraft/core/Holder;I)Lnet/minecraft/network/chat/Component;"))
    private Component gbw$addElementSymbol(Holder<Enchantment> enchantment, int level, Operation<Component> original) {
        Component name = original.call(enchantment, level);
        Element element = ElementHolder.gbw$getElement(enchantment.unwrapKey().orElseThrow());
        if (element != null)
            return ComponentUtils.formatList(List.of(element.getSymbol(), name), Component.literal(" "));
        return name;
    }

}
