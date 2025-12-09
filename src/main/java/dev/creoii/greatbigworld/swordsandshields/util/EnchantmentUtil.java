package dev.creoii.greatbigworld.swordsandshields.util;

import dev.creoii.greatbigworld.element.Element;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsDataComponentTypes;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.entry.RegistryEntry;

public final class EnchantmentUtil {
    public static boolean canBeCombined(RegistryEntry<Enchantment> first, RegistryEntry<Enchantment> second) {
        Element a = ElementHolder.gbw$getElement(first.getKey().orElseThrow());
        Element b = ElementHolder.gbw$getElement(first.getKey().orElseThrow());

        if (a != null && b != null && (a == b || a.getSynergy() == null || a.getSynergy() == b || b.getSynergy() == null || b.getSynergy() == a)) {
            return !first.equals(second) && !first.value().exclusiveSet().contains(second) && !second.value().exclusiveSet().contains(first);
        }

        return false;
    }

    public static int getEnchantmentPower(ItemStack stack) {
        int enchantmentPower = 0;

        if (stack.contains(DataComponentTypes.ENCHANTMENTS)) {
            enchantmentPower = getEnchantmentPower(stack.get(DataComponentTypes.ENCHANTMENTS));
        }

        return enchantmentPower;
    }

    public static int getEnchantmentPower(ItemEnchantmentsComponent component) {
        int enchantmentPower = 0;

        if (component != null && !component.isEmpty()) {
            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : component.getEnchantmentEntries()) {
                enchantmentPower += entry.getIntValue();
            }
        }

        return enchantmentPower;
    }

    public static int getEnchantmentLevelCap(ItemStack stack) {
        String id = stack.get(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL);

        if (id == null || id.isBlank())
            return 0;

        if (stack.contains(DataComponentTypes.TOOL)) {
            ToolMaterial toolMaterial = EquipmentMaterialUtil.getToolMaterial(id);
            EquipmentMaterialUtil.Entry entry = EquipmentMaterialUtil.TOOL_ENTRIES.get(toolMaterial);
            if (entry != null)
                return entry.totalEnchantmentLevelCap();
        } else {
            EquipmentMaterialUtil.Entry entry = EquipmentMaterialUtil.ARMOR_ENTRIES.get(EquipmentMaterialUtil.getArmorMaterial(id));
            if (entry != null)
                return entry.totalEnchantmentLevelCap();
        }
        return 0;
    }
}
