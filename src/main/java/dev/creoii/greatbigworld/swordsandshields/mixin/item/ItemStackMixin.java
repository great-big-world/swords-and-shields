package dev.creoii.greatbigworld.swordsandshields.mixin.item;

import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsDataComponentTypes;
import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder {
    @Inject(method = "addDetailsToTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V", ordinal = 3))
    private void gbw$appendEquipmentTooltips(Item.TooltipContext context, TooltipDisplay displayComponent, Player player, TooltipFlag type, Consumer<Component> textConsumer, CallbackInfo ci) {
        if (has(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL)) {
            int totalEnchantmentLevelCap = EnchantmentUtil.getEnchantmentLevelCap((ItemStack) (Object) this);

            if (totalEnchantmentLevelCap < 0)
                return;

            int enchantmentPower = EnchantmentUtil.getEnchantmentPower((ItemStack) (Object) this);

            ChatFormatting formatting = ChatFormatting.GRAY;
            if (enchantmentPower >= totalEnchantmentLevelCap)
                formatting = ChatFormatting.GOLD;

            textConsumer.accept(Component.translatable("tooltip.enchantment_power", enchantmentPower, totalEnchantmentLevelCap).withStyle(formatting));
        }
    }
}
