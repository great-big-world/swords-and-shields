package dev.creoii.greatbigworld.swordsandshields.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.client.GreatBigWorldClient;
import dev.creoii.greatbigworld.element.Element;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.swordsandshields.util.ElementHolder;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Set;

@Mixin(EnchantmentScreen.class)
public class EnchantmentScreenMixin {
    @WrapOperation(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/EnchantmentScreenHandler;getLapisCount()I"))
    private int gbw$ignoreLapisCount(EnchantmentScreenHandler instance, Operation<Integer> original) {
        Set<Knowledge> knowledge = GreatBigWorldClient.getKnowledge().getOrDefault(Knowledge.Type.ENCHANTMENT, null);

        if (knowledge == null)
            return 0;

        return Math.min(3, knowledge.size());
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;getName(Lnet/minecraft/registry/entry/RegistryEntry;I)Lnet/minecraft/text/Text;"))
    private Text gbw$addElementSymbol(RegistryEntry<Enchantment> enchantment, int level, Operation<Text> original) {
        Text name = original.call(enchantment, level);
        Element element = ElementHolder.gbw$getElement(enchantment.getKey().orElseThrow());
        if (element != null)
            return Texts.join(List.of(element.getSymbol(), name), Text.literal(" "));
        return name;
    }

}
