package dev.creoii.greatbigworld.swordsandshields.mixin.item;


import dev.creoii.greatbigworld.swordsandshields.util.EquipmentMaterial;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ToolMaterial.class)
public record ToolMaterialMixin() implements EquipmentMaterial {
    @Override
    public int gbw$maxEnchantmentLevel() {
        return TOOL_ENTRIES.get((ToolMaterial) (Object) this).maxEnchantmentLevel();
    }

    @Override
    public int gbw$totalEnchantmentLevelCap() {
        return TOOL_ENTRIES.get((ToolMaterial) (Object) this).totalEnchantmentLevelCap();
    }
}
