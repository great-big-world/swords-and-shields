package dev.creoii.greatbigworld.swordsandshields;

import dev.creoii.greatbigworld.mixin.AbstractBlockStateAccessor;
import dev.creoii.greatbigworld.swordsandshields.util.StruckByLightningS2C;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.block.Blocks;

public class SwordsAndShields implements ModInitializer {
    @Override
    public void onInitialize() {
        ((AbstractBlockStateAccessor) Blocks.ENCHANTING_TABLE.getDefaultState()).setHardness(15f);
        ((AbstractBlockStateAccessor) Blocks.SPAWNER.getDefaultState()).setLuminance(7);
        ((AbstractBlockStateAccessor) Blocks.TRIAL_SPAWNER.getDefaultState()).setLuminance(7);

        PayloadTypeRegistry.playS2C().register(StruckByLightningS2C.PACKET_ID, StruckByLightningS2C.PACKET_CODEC);
    }
}
