package dev.creoii.greatbigworld.swordsandshields.item;

import dev.creoii.greatbigworld.swordsandshields.util.EquipmentMaterialUtil;
import net.minecraft.world.item.equipment.ArmorMaterials;

public final class SwordsAndShieldsArmorMaterials {
    public static void register() {
        EquipmentMaterialUtil.register(ArmorMaterials.LEATHER, 1, 2);
        EquipmentMaterialUtil.register(ArmorMaterials.CHAINMAIL, 3, 6);
        EquipmentMaterialUtil.register(ArmorMaterials.COPPER, 4, 9);
        EquipmentMaterialUtil.register(ArmorMaterials.IRON, 5, 13);
        EquipmentMaterialUtil.register(ArmorMaterials.GOLD, 9, 45);
        EquipmentMaterialUtil.register(ArmorMaterials.DIAMOND, 7, 32);
        EquipmentMaterialUtil.register(ArmorMaterials.NETHERITE, 6, 21);
        EquipmentMaterialUtil.register(ArmorMaterials.ARMADILLO_SCUTE, 0, 0);
        EquipmentMaterialUtil.register(ArmorMaterials.TURTLE_SCUTE, 0, 0);
    }
}
