package dev.creoii.greatbigworld.swordsandshields;

import dev.creoii.greatbigworld.mixin.AbstractBlockStateAccessor;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;

public class SwordsAndShields implements ModInitializer {
    @Override
    public void onInitialize() {
        ((AbstractBlockStateAccessor) Blocks.ENCHANTING_TABLE.getDefaultState()).setHardness(15f);
        ((AbstractBlockStateAccessor) Blocks.SPAWNER.getDefaultState()).setLuminance(7);
        ((AbstractBlockStateAccessor) Blocks.TRIAL_SPAWNER.getDefaultState()).setLuminance(7);
    }
}
