package dev.creoii.greatbigworld.swordsandshields.item;

import dev.creoii.greatbigworld.swordsandshields.util.EquipmentMaterial;
import net.minecraft.item.equipment.ArmorMaterials;

public final class SwordsAndShieldsArmorMaterials {
    public static void register() {
        EquipmentMaterial.register(ArmorMaterials.LEATHER, 1, 2);
    }
}
