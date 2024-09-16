package dev.creoii.greatbigworld.swordsandshields.registry;

import dev.creoii.greatbigworld.swordsandshields.SwordsAndShields;
import dev.creoii.greatbigworld.swordsandshields.block.EnchantedStoneBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class SwordsAndShieldBlocks {
    public static final Block ENCHANTED_STONE = new EnchantedStoneBlock();

    public static void register() {
        Registry.register(Registries.BLOCK, new Identifier(SwordsAndShields.NAMESPACE, "enchanted_stone"), ENCHANTED_STONE);
    }
}
