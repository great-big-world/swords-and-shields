package dev.creoii.greatbigworld.swordsandshields.mixin.item;


import dev.creoii.greatbigworld.swordsandshields.util.EquipmentMaterial;
import net.minecraft.item.equipment.ArmorMaterial;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArmorMaterial.class)
public record ArmorMaterialMixin() implements EquipmentMaterial {
    @Override
    public int gbw$maxEnchantmentLevel() {
        return ARMOR_ENTRIES.get((ArmorMaterial) (Object) this).maxEnchantmentLevel();
    }

    @Override
    public int gbw$totalEnchantmentLevelCap() {
        return ARMOR_ENTRIES.get((ArmorMaterial) (Object) this).totalEnchantmentLevelCap();
    }
}
