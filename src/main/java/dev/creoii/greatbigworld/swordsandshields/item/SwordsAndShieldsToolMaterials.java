package dev.creoii.greatbigworld.swordsandshields.item;

import dev.creoii.greatbigworld.swordsandshields.util.EquipmentMaterialUtil;
import net.minecraft.world.item.ToolMaterial;

public final class SwordsAndShieldsToolMaterials {
    public static void register() {
        EquipmentMaterialUtil.register(ToolMaterial.WOOD, 1, 2);
        EquipmentMaterialUtil.register(ToolMaterial.STONE, 3, 6);
        EquipmentMaterialUtil.register(ToolMaterial.COPPER, 4, 9);
        EquipmentMaterialUtil.register(ToolMaterial.IRON, 5, 13);
        EquipmentMaterialUtil.register(ToolMaterial.GOLD, 9, 45);
        EquipmentMaterialUtil.register(ToolMaterial.DIAMOND, 7, 32);
        EquipmentMaterialUtil.register(ToolMaterial.NETHERITE, 6, 21);
    }
}
