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
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;

public final class SwordsAndShieldsItems {
    //public static Item BRISK_FLAKE;
    //public static Item TORRENT_BALL;
    //public static Item TEMPEST_BOLT;
    //public static Item BORE_ROCK;
    // breeze charge
    // blaze powder
    public static Item OAK_MASK;
    public static Item SPRUCE_MASK;
    public static Item BIRCH_MASK;
    public static Item JUNGLE_MASK;
    public static Item ACACIA_MASK;
    public static Item DARK_OAK_MASK;
    public static Item MANGROVE_MASK;
    public static Item CHERRY_MASK;
    public static Item BAMBOO_MASK;
    public static Item ACAI_MASK;
    public static Item ASPEN_MASK;
    public static Item CRIMSON_MASK;
    public static Item WARPED_MASK;

    public static Item LEATHER_MASK;
    public static Item CHAINMAIL_MASK;
    public static Item COPPER_MASK;
    public static Item IRON_MASK;
    public static Item GOLDEN_MASK;
    public static Item DIAMOND_MASK;
    public static Item NETHERITE_MASK;

    public static Item ILLUSIONER_SPAWN_EGG;

    public static void register() {
        // elemental items

        OAK_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "oak_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));
        SPRUCE_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "spruce_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));
        BIRCH_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "birch_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));
        JUNGLE_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "jungle_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));
        ACACIA_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "acacia_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));
        DARK_OAK_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "dark_oak_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));
        MANGROVE_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "mangrove_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));
        CHERRY_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "cherry_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));
        BAMBOO_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "bamboo_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));
        ACAI_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "acai_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));
        ASPEN_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "aspen_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));
        CRIMSON_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "crimson_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));
        WARPED_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "warped_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));

        LEATHER_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "leather_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.LEATHER, ArmorType.HELMET));
        CHAINMAIL_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "chainmail_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.HELMET));
        COPPER_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "copper_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.COPPER, ArmorType.HELMET));
        IRON_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "iron_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.IRON, ArmorType.HELMET));
        GOLDEN_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "golden_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.GOLD, ArmorType.HELMET));
        DIAMOND_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "diamond_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET));
        NETHERITE_MASK = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "netherite_mask"), new Item.Properties().humanoidArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET));

        ILLUSIONER_SPAWN_EGG = RegistryHelper.registerItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "illusioner_spawn_egg"), SpawnEggItem::new, new Item.Properties().spawnEgg(EntityType.ILLUSIONER));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> {
            entries.addAfter(Items.EVOKER_SPAWN_EGG, ILLUSIONER_SPAWN_EGG);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(entries -> {
            entries.addAfter(Items.NETHERITE_NAUTILUS_ARMOR, OAK_MASK, SPRUCE_MASK, BIRCH_MASK, JUNGLE_MASK, ACACIA_MASK, DARK_OAK_MASK, MANGROVE_MASK, CHERRY_MASK, BAMBOO_MASK, ACAI_MASK, ASPEN_MASK, CRIMSON_MASK, WARPED_MASK);
            entries.addAfter(Items.LEATHER_HELMET, LEATHER_MASK);
            entries.addAfter(Items.CHAINMAIL_HELMET, CHAINMAIL_MASK);
            entries.addAfter(Items.COPPER_HELMET, COPPER_MASK);
            entries.addAfter(Items.IRON_HELMET, IRON_MASK);
            entries.addAfter(Items.GOLDEN_HELMET, GOLDEN_MASK);
            entries.addAfter(Items.DIAMOND_HELMET, DIAMOND_MASK);
            entries.addAfter(Items.NETHERITE_HELMET, NETHERITE_MASK);
        });
    }
}
