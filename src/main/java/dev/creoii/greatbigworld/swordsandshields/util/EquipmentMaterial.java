package dev.creoii.greatbigworld.swordsandshields.util;

import net.minecraft.item.ToolMaterial;
import net.minecraft.item.equipment.ArmorMaterial;

import java.util.HashMap;
import java.util.Map;

public interface EquipmentMaterial {
    Map<ArmorMaterial, Entry> ARMOR_ENTRIES = new HashMap<>();
    Map<ToolMaterial, Entry> TOOL_ENTRIES = new HashMap<>();

    int gbw$maxEnchantmentLevel();

    int gbw$totalEnchantmentLevelCap();

    static void register(ArmorMaterial armorMaterial, int maxEnchantmentLevel, int totalEnchantmentLevelCap) {
        ARMOR_ENTRIES.putIfAbsent(armorMaterial, new Entry(maxEnchantmentLevel, totalEnchantmentLevelCap));
    }

    static void register(ToolMaterial toolMaterial, int maxEnchantmentLevel, int totalEnchantmentLevelCap) {
        TOOL_ENTRIES.putIfAbsent(toolMaterial, new Entry(maxEnchantmentLevel, totalEnchantmentLevelCap));
    }

    static EquipmentMaterial of(ArmorMaterial armorMaterial) {
        if ((Object) armorMaterial instanceof EquipmentMaterial extendedArmorMaterial) {
            return extendedArmorMaterial;
        }
        throw new IllegalStateException("This shouldn't happen.");
    }

    record Entry(int maxEnchantmentLevel, int totalEnchantmentLevelCap) {
    }
}
