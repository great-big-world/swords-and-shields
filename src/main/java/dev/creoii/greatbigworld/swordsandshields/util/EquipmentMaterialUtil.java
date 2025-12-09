package dev.creoii.greatbigworld.swordsandshields.util;

import net.minecraft.item.ToolMaterial;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class EquipmentMaterialUtil {
    public static Map<ArmorMaterial, Entry> ARMOR_ENTRIES = new HashMap<>();
    public static Map<ToolMaterial, Entry> TOOL_ENTRIES = new HashMap<>();
    public static PacketCodec<RegistryByteBuf, String> PACKET_CODEC = PacketCodec.of((value, buf) -> buf.writeString(value), PacketByteBuf::readString);

    @Nullable
    static ToolMaterial getToolMaterial(String id) {
        return switch (id) {
            case "wood" -> ToolMaterial.WOOD;
            case "stone" -> ToolMaterial.STONE;
            case "iron" -> ToolMaterial.IRON;
            case "gold" -> ToolMaterial.GOLD;
            case "diamond" -> ToolMaterial.DIAMOND;
            case "netherite" -> ToolMaterial.NETHERITE;
            case "copper" -> ToolMaterial.COPPER;
            default -> null;
        };
    }

    @Nullable
    static ArmorMaterial getArmorMaterial(String id) {
        return switch (id) {
            case "leather" -> ArmorMaterials.LEATHER;
            case "chain" -> ArmorMaterials.CHAIN;
            case "iron" -> ArmorMaterials.IRON;
            case "gold" -> ArmorMaterials.GOLD;
            case "diamond" -> ArmorMaterials.DIAMOND;
            case "netherite" -> ArmorMaterials.NETHERITE;
            case "copper" -> ArmorMaterials.COPPER;
            case "turtle_scute" -> ArmorMaterials.TURTLE_SCUTE;
            case "armadillo_scute" -> ArmorMaterials.ARMADILLO_SCUTE;
            default -> null;
        };
    }

    public static void register(ArmorMaterial armorMaterial, int maxEnchantmentLevel, int totalEnchantmentLevelCap) {
        ARMOR_ENTRIES.put(armorMaterial, new Entry(maxEnchantmentLevel, totalEnchantmentLevelCap));
    }

    public static void register(ToolMaterial toolMaterial, int maxEnchantmentLevel, int totalEnchantmentLevelCap) {
        TOOL_ENTRIES.put(toolMaterial, new Entry(maxEnchantmentLevel, totalEnchantmentLevelCap));
    }

    public record Entry(int maxEnchantmentLevel, int totalEnchantmentLevelCap) {
    }
}
