package dev.creoii.greatbigworld.swordsandshields;

import dev.creoii.greatbigworld.mixin.AbstractBlockStateAccessor;
import dev.creoii.greatbigworld.swordsandshields.item.SwordsAndShieldsArmorMaterials;
import dev.creoii.greatbigworld.swordsandshields.item.SwordsAndShieldsToolMaterials;
import dev.creoii.greatbigworld.swordsandshields.registry.*;
import dev.creoii.greatbigworld.swordsandshields.util.ElementHolder;
import dev.creoii.greatbigworld.swordsandshields.util.EquipmentUpgrading;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.level.block.Blocks;

public class SwordsAndShields implements ModInitializer {
    @Override
    public void onInitialize() {
        SwordsAndShieldsItems.register();
        SwordsAndShieldsArmorMaterials.register();
        SwordsAndShieldsToolMaterials.register();
        SwordsAndShieldsDataComponentTypes.register();
        SwordsAndShieldsRecipes.register();
        SwordsAndShieldsScreens.register();
        SwordsAndShieldsEvents.register();
        EquipmentUpgrading.register();
        ElementHolder.register();

        ((AbstractBlockStateAccessor) Blocks.ENCHANTING_TABLE.defaultBlockState()).setDestroySpeed(15f);
        ((AbstractBlockStateAccessor) Blocks.SPAWNER.defaultBlockState()).setLightEmission(4);
    }
}
