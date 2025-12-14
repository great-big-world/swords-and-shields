package dev.creoii.greatbigworld.swordsandshields.item;

import dev.creoii.greatbigworld.swordsandshields.util.EquipmentMaterialUtil;
import net.minecraft.world.item.ToolMaterial;

public final class SwordsAndShieldsToolMaterials {
    public static void register() {
        EquipmentMaterialUtil.register(ToolMaterial.WOOD, 1, 2, 2);
        EquipmentMaterialUtil.register(ToolMaterial.STONE, 3, 6, 1);
        EquipmentMaterialUtil.register(ToolMaterial.COPPER, 4, 9, 2);
        EquipmentMaterialUtil.register(ToolMaterial.IRON, 5, 13, 3);
        EquipmentMaterialUtil.register(ToolMaterial.GOLD, 9, 45, 4);
        EquipmentMaterialUtil.register(ToolMaterial.DIAMOND, 7, 32, 2);
        EquipmentMaterialUtil.register(ToolMaterial.NETHERITE, 6, 21, 4);
    }
}
