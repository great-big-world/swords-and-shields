package dev.creoii.greatbigworld.swordsandshields.registry;

import dev.creoii.greatbigworld.swordsandshields.SwordsAndShields;
import dev.creoii.greatbigworld.swordsandshields.block.EnchantedStoneBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class SwordsAndShieldsBlockEntities {
    public static final BlockEntityType<EnchantedStoneBlockEntity> ENCHANTED_STONE = BlockEntityType.Builder.create(EnchantedStoneBlockEntity::new, SwordsAndShieldBlocks.ENCHANTED_STONE, SwordsAndShieldBlocks.ENCHANTED_DEEPSLATE).build();

    public static void register() {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(SwordsAndShields.NAMESPACE, "enchanted_stone"), ENCHANTED_STONE);
    }
}
