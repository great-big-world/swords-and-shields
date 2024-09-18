package dev.creoii.greatbigworld.swordsandshields.registry;

import dev.creoii.greatbigworld.swordsandshields.SwordsAndShields;
import dev.creoii.greatbigworld.swordsandshields.block.EnchantedStoneBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class SwordsAndShieldBlocks {
    public static final Block ENCHANTED_STONE = new EnchantedStoneBlock();
    public static final Block ENCHANTED_DEEPSLATE = new EnchantedStoneBlock();

    public static void register() {
        Registry.register(Registries.BLOCK, new Identifier(SwordsAndShields.NAMESPACE, "enchanted_stone"), ENCHANTED_STONE);
        Registry.register(Registries.BLOCK, new Identifier(SwordsAndShields.NAMESPACE, "enchanted_deepslate"), ENCHANTED_DEEPSLATE);
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), ENCHANTED_STONE, ENCHANTED_DEEPSLATE);
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (tintIndex != 0) {
                return switch (state.get(EnchantedStoneBlock.GLOW)) {
                    case 0 -> 5921370;
                    case 1 -> 10194851;
                    case 2 -> 12824784;
                    case 3 -> 15325173;
                    default -> -1;
                };
            } else return -1;
        }, ENCHANTED_STONE, ENCHANTED_DEEPSLATE);
    }
}
