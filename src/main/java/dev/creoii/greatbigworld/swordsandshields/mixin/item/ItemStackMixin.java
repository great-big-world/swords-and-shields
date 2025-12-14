package dev.creoii.greatbigworld.swordsandshields.mixin.item;

import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsDataComponentTypes;
import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentUtil;
import dev.creoii.greatbigworld.swordsandshields.util.EquipmentMaterialUtil;
import net.minecraft.core.component.DataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
            if (has(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES))
                appendEquipmentUpgrades(textConsumer);
            appendEnchantmentPower(textConsumer);
        }
    }

    @Unique
    private void appendEnchantmentPower(Consumer<Component> textConsumer) {
        ItemStack stack = (ItemStack) (Object) this;
        int totalEnchantmentLevelCap = EnchantmentUtil.getEnchantmentLevelCap(stack);

        if (totalEnchantmentLevelCap < 0)
            return;

        int enchantmentPower = EnchantmentUtil.getEnchantmentPower(stack);

        ChatFormatting formatting = ChatFormatting.GRAY;
        if (enchantmentPower >= totalEnchantmentLevelCap)
            formatting = ChatFormatting.GOLD;

        textConsumer.accept(Component.translatable("tooltip.enchantment_power", enchantmentPower, totalEnchantmentLevelCap).withStyle(formatting));
    }

    @Unique
    private void appendEquipmentUpgrades(Consumer<Component> textConsumer) {
        ItemStack stack = (ItemStack) (Object) this;
        String material = stack.get(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL);
        int maxUpgrades = stack.has(DataComponents.TOOL) ? EquipmentMaterialUtil.TOOL_ENTRIES.get(EquipmentMaterialUtil.getToolMaterial(material)).maxUpgrades() : EquipmentMaterialUtil.ARMOR_ENTRIES.get(EquipmentMaterialUtil.getArmorMaterial(material)).maxUpgrades();

        if (maxUpgrades <= 0)
            return;

        int upgrades = stack.get(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES);

        ChatFormatting formatting = ChatFormatting.GRAY;
        if (upgrades >= maxUpgrades)
            formatting = ChatFormatting.GOLD;

        textConsumer.accept(Component.translatable("tooltip.equipment_upgrades", upgrades, maxUpgrades).withStyle(formatting));
    }
}
