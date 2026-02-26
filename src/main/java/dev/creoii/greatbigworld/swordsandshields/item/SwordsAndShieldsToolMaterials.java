package dev.creoii.greatbigworld.swordsandshields.item;

import dev.creoii.greatbigworld.swordsandshields.util.EquipmentMaterialUtil;
import net.minecraft.world.item.ToolMaterial;

public final class SwordsAndShieldsToolMaterials {
    public static void register() {
        EquipmentMaterialUtil.register(ToolMaterial.WOOD, 1, 2, 2);
        EquipmentMaterialUtil.register(ToolMaterial.STONE, 2, 4, 1);
        EquipmentMaterialUtil.register(ToolMaterial.COPPER, 4, 5, 3);
        EquipmentMaterialUtil.register(ToolMaterial.IRON, 5, 7, 4);
        EquipmentMaterialUtil.register(ToolMaterial.GOLD, 9, 19, 5);
        EquipmentMaterialUtil.register(ToolMaterial.DIAMOND, 7, 13, 3);
        EquipmentMaterialUtil.register(ToolMaterial.NETHERITE, 6, 11, 4);
    }
}
