package dev.creoii.greatbigworld.swordsandshields.registry;

import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.swordsandshields.util.EquipmentMaterialUtil;
import dev.creoii.greatbigworld.swordsandshields.util.EquipmentUpgrading;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public final class SwordsAndShieldsDataComponentTypes {
    public static DataComponentType<String> EQUIPMENT_MATERIAL;
    public static DataComponentType<Integer> EQUIPMENT_UPGRADES;

    public static void register() {
        EQUIPMENT_MATERIAL = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "equipment_material"), DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(EquipmentMaterialUtil.PACKET_CODEC).build());
        EQUIPMENT_UPGRADES = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "equipment_upgrades"), DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(EquipmentUpgrading.UPGRADE_PACKET_CODEC).build());
    }
}
