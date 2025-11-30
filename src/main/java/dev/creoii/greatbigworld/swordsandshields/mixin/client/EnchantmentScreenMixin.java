package dev.creoii.greatbigworld.swordsandshields.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.client.GreatBigWorldClient;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.screen.EnchantmentScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

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
}
