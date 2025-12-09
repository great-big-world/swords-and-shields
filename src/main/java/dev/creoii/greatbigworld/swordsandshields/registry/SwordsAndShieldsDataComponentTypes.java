package dev.creoii.greatbigworld.swordsandshields.registry;

import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.swordsandshields.util.EquipmentMaterialUtil;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class SwordsAndShieldsDataComponentTypes {
    public static ComponentType<String> EQUIPMENT_MATERIAL;

    public static void register() {
        EQUIPMENT_MATERIAL = Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "equipment_material"), ComponentType.<String>builder().codec(Codec.STRING).packetCodec(EquipmentMaterialUtil.PACKET_CODEC).build());
    }
}
