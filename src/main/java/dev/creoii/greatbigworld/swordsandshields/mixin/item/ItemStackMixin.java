package dev.creoii.greatbigworld.swordsandshields.mixin.item;

import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsDataComponentTypes;
import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentUtil;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder {
    @Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendComponentTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Lnet/minecraft/component/type/TooltipDisplayComponent;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V", ordinal = 3))
    private void gbw$appendEquipmentTooltips(Item.TooltipContext context, TooltipDisplayComponent displayComponent, PlayerEntity player, TooltipType type, Consumer<Text> textConsumer, CallbackInfo ci) {
        if (contains(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL)) {
            int totalEnchantmentLevelCap = EnchantmentUtil.getEnchantmentLevelCap((ItemStack) (Object) this);

            if (totalEnchantmentLevelCap < 0)
                return;

            int enchantmentPower = EnchantmentUtil.getEnchantmentPower((ItemStack) (Object) this);

            Formatting formatting = Formatting.GRAY;
            if (enchantmentPower >= totalEnchantmentLevelCap)
                formatting = Formatting.GOLD;

            textConsumer.accept(Text.translatable("tooltip.enchantment_power", enchantmentPower, totalEnchantmentLevelCap).formatted(formatting));
        }
    }
}
