package dev.creoii.greatbigworld.swordsandshields.item;

import dev.creoii.greatbigworld.swordsandshields.util.EquipmentMaterialUtil;
import net.minecraft.world.item.equipment.ArmorMaterials;

public final class SwordsAndShieldsArmorMaterials {
    public static void register() {
        EquipmentMaterialUtil.register(ArmorMaterials.LEATHER, 1, 2, 2);
        EquipmentMaterialUtil.register(ArmorMaterials.CHAINMAIL, 2, 4, 1);
        EquipmentMaterialUtil.register(ArmorMaterials.COPPER, 4, 5, 3);
        EquipmentMaterialUtil.register(ArmorMaterials.IRON, 5, 7, 4);
        EquipmentMaterialUtil.register(ArmorMaterials.GOLD, 9, 19, 5);
        EquipmentMaterialUtil.register(ArmorMaterials.DIAMOND, 7, 13, 3);
        EquipmentMaterialUtil.register(ArmorMaterials.NETHERITE, 6, 11, 4);
        EquipmentMaterialUtil.register(ArmorMaterials.ARMADILLO_SCUTE, 0, 0, 0);
        EquipmentMaterialUtil.register(ArmorMaterials.TURTLE_SCUTE, 3, 10, 3);
    }
}
