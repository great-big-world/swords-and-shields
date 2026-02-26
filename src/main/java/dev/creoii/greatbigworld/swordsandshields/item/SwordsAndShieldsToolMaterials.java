package dev.creoii.greatbigworld.swordsandshields.item;

import dev.creoii.greatbigworld.swordsandshields.util.EquipmentMaterialUtil;
import net.minecraft.world.item.ToolMaterial;

public final class SwordsAndShieldsToolMaterials {
    public static void register() {
        EquipmentMaterialUtil.register(ToolMaterial.WOOD, 1, 2, 2);
        EquipmentMaterialUtil.register(ToolMaterial.STONE, 2, 4, 1);
        EquipmentMaterialUtil.register(ToolMaterial.COPPER, 4, 5, 4);
        EquipmentMaterialUtil.register(ToolMaterial.IRON, 5, 7, 5);
        EquipmentMaterialUtil.register(ToolMaterial.GOLD, 5, 19, 5);
        EquipmentMaterialUtil.register(ToolMaterial.DIAMOND, 5, 13, 3);
        EquipmentMaterialUtil.register(ToolMaterial.NETHERITE, 5, 11, 5);
        EquipmentMaterialUtil.register(EquipmentMaterialUtil.MACE, 5, 12, 5);
        EquipmentMaterialUtil.register(EquipmentMaterialUtil.TRIDENT, 5, 11, 3);
        EquipmentMaterialUtil.register(EquipmentMaterialUtil.SHIELD, 3, 4, 4);
    }
}
