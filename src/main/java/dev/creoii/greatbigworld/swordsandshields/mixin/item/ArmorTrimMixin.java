package dev.creoii.greatbigworld.swordsandshields.mixin.item;

import dev.creoii.greatbigworld.swordsandshields.util.SwordsAndShieldsTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ArmorTrim.class)
public record ArmorTrimMixin(Holder<TrimMaterial> material, Holder<TrimPattern> pattern) {
    @Inject(method = "addToTooltip", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 1), cancellable = true)
    private void gbw$reformatArmorTrimTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter, CallbackInfo ci) {
        if (material.is(SwordsAndShieldsTags.DECORATION)) {
            consumer.accept(CommonComponents.space().append(material.value().description()).withStyle(ChatFormatting.GRAY));
        } else consumer.accept(CommonComponents.space().append(material.value().description()).append(CommonComponents.space().append(pattern.value().copyWithStyle(material))));
        ci.cancel();
    }
}
