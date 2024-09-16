package dev.creoii.greatbigworld.swordsandshields.registry;

import dev.creoii.creoapi.api.item.CreoItemSettings;
import dev.creoii.greatbigworld.swordsandshields.SwordsAndShields;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public final class SwordsAndShieldItems {
    public static final Item ENCHANTED_STONE = new BlockItem(SwordsAndShieldBlocks.ENCHANTED_STONE, new CreoItemSettings().rarity(Rarity.UNCOMMON));

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(SwordsAndShields.NAMESPACE, "enchanted_stone"), ENCHANTED_STONE);
    }
}
