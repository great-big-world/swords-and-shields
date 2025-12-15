package dev.creoii.greatbigworld.swordsandshields.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.util.RegistryHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;

public final class SwordsAndShieldsItems {
    //public static Item BRISK_FLAKE;
    //public static Item TORRENT_BALL;
    //public static Item TEMPEST_BOLT;
    //public static Item BORE_ROCK;
    // breeze charge
    // blaze powder

    public static Item ILLUSIONER_SPAWN_EGG;

    public static void register() {
        // elemental items

        ILLUSIONER_SPAWN_EGG = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "illusioner_spawn_egg"), SpawnEggItem::new, new Item.Properties().spawnEgg(EntityType.ILLUSIONER));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> {
            entries.addAfter(Items.EVOKER_SPAWN_EGG, ILLUSIONER_SPAWN_EGG);
        });
    }
}
