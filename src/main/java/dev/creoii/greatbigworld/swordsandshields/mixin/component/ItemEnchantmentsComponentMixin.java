package dev.creoii.greatbigworld.swordsandshields.mixin.component;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.element.Element;
import dev.creoii.greatbigworld.swordsandshields.util.ElementHolder;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ItemEnchantmentsComponent.class)
public class ItemEnchantmentsComponentMixin {
    @WrapOperation(method = "appendTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;getName(Lnet/minecraft/registry/entry/RegistryEntry;I)Lnet/minecraft/text/Text;"))
    private Text gbw$addElementSymbol(RegistryEntry<Enchantment> enchantment, int level, Operation<Text> original) {
        Text name = original.call(enchantment, level);
        Element element = ElementHolder.gbw$getElement(enchantment.getKey().orElseThrow());
        if (element != null)
            return Texts.join(List.of(element.getSymbol(), name), Text.literal(""));
        return name;
    }
}
