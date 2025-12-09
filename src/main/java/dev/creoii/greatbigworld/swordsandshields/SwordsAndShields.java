package dev.creoii.greatbigworld.swordsandshields;

import com.google.common.collect.ImmutableList;
import dev.creoii.greatbigworld.element.Element;
import dev.creoii.greatbigworld.mixin.AbstractBlockStateAccessor;
import dev.creoii.greatbigworld.swordsandshields.item.SwordsAndShieldsArmorMaterials;
import dev.creoii.greatbigworld.swordsandshields.item.SwordsAndShieldsToolMaterials;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsDataComponentTypes;
import dev.creoii.greatbigworld.swordsandshields.util.ElementHolder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;

import java.util.List;

public class SwordsAndShields implements ModInitializer {
    private static final List<Item> MEATS = new ImmutableList.Builder<Item>()
            .add(Items.BEEF).add(Items.COOKED_BEEF)
            .add(Items.PORKCHOP).add(Items.COOKED_PORKCHOP)
            .add(Items.CHICKEN).add(Items.COOKED_CHICKEN)
            .add(Items.MUTTON).add(Items.COOKED_MUTTON)
            .add(Items.COD).add(Items.COOKED_COD)
            .add(Items.SALMON).add(Items.COOKED_SALMON)
            .build();
    private static final List<Item> PIES = new ImmutableList.Builder<Item>()
            .add(Items.PUMPKIN_PIE)
            .build();
    private static final List<Item> NETHERITE_EQUIPMENT = new ImmutableList.Builder<Item>()
            .add(Items.NETHERITE_PICKAXE)
            .add(Items.NETHERITE_SWORD)
            .add(Items.NETHERITE_AXE)
            .add(Items.NETHERITE_SHOVEL)
            .add(Items.NETHERITE_HOE)
            .add(Items.NETHERITE_HELMET)
            .add(Items.NETHERITE_CHESTPLATE)
            .add(Items.NETHERITE_LEGGINGS)
            .add(Items.NETHERITE_BOOTS)
            .build();
    private static final List<Item> DIAMOND_EQUIPMENT = new ImmutableList.Builder<Item>()
            .add(Items.DIAMOND_PICKAXE)
            .add(Items.DIAMOND_SWORD)
            .add(Items.DIAMOND_AXE)
            .add(Items.DIAMOND_SHOVEL)
            .add(Items.DIAMOND_HOE)
            .add(Items.DIAMOND_HELMET)
            .add(Items.DIAMOND_CHESTPLATE)
            .add(Items.DIAMOND_LEGGINGS)
            .add(Items.DIAMOND_BOOTS)
            .build();
    private static final List<Item> GOLD_EQUIPMENT = new ImmutableList.Builder<Item>()
            .add(Items.GOLDEN_PICKAXE)
            .add(Items.GOLDEN_SWORD)
            .add(Items.GOLDEN_AXE)
            .add(Items.GOLDEN_SHOVEL)
            .add(Items.GOLDEN_HOE)
            .add(Items.GOLDEN_HELMET)
            .add(Items.GOLDEN_CHESTPLATE)
            .add(Items.GOLDEN_LEGGINGS)
            .add(Items.GOLDEN_BOOTS)
            .build();
    private static final List<Item> IRON_EQUIPMENT = new ImmutableList.Builder<Item>()
            .add(Items.IRON_PICKAXE)
            .add(Items.IRON_SWORD)
            .add(Items.IRON_AXE)
            .add(Items.IRON_SHOVEL)
            .add(Items.IRON_HOE)
            .add(Items.IRON_HELMET)
            .add(Items.IRON_CHESTPLATE)
            .add(Items.IRON_LEGGINGS)
            .add(Items.IRON_BOOTS)
            .build();
    private static final List<Item> COPPER_EQUIPMENT = new ImmutableList.Builder<Item>()
            .add(Items.COPPER_PICKAXE)
            .add(Items.COPPER_SWORD)
            .add(Items.COPPER_AXE)
            .add(Items.COPPER_SHOVEL)
            .add(Items.COPPER_HOE)
            .add(Items.COPPER_HELMET)
            .add(Items.COPPER_CHESTPLATE)
            .add(Items.COPPER_LEGGINGS)
            .add(Items.COPPER_BOOTS)
            .build();
    private static final List<Item> STONE_EQUIPMENT = new ImmutableList.Builder<Item>()
            .add(Items.STONE_PICKAXE)
            .add(Items.STONE_SWORD)
            .add(Items.STONE_AXE)
            .add(Items.STONE_SHOVEL)
            .add(Items.STONE_HOE)
            .build();
    private static final List<Item> WOOD_EQUIPMENT = new ImmutableList.Builder<Item>()
            .add(Items.WOODEN_PICKAXE)
            .add(Items.WOODEN_SWORD)
            .add(Items.WOODEN_AXE)
            .add(Items.WOODEN_SHOVEL)
            .add(Items.WOODEN_HOE)
            .build();
    private static final List<Item> CHAIN_EQUIPMENT = new ImmutableList.Builder<Item>()
            .add(Items.CHAINMAIL_HELMET)
            .add(Items.CHAINMAIL_CHESTPLATE)
            .add(Items.CHAINMAIL_LEGGINGS)
            .add(Items.CHAINMAIL_BOOTS)
            .build();
    private static final List<Item> LEATHER_EQUIPMENT = new ImmutableList.Builder<Item>()
            .add(Items.LEATHER_HELMET)
            .add(Items.LEATHER_CHESTPLATE)
            .add(Items.LEATHER_LEGGINGS)
            .add(Items.LEATHER_BOOTS)
            .build();

    @Override
    public void onInitialize() {
        SwordsAndShieldsArmorMaterials.register();
        SwordsAndShieldsToolMaterials.register();
        SwordsAndShieldsDataComponentTypes.register();

        ((AbstractBlockStateAccessor) Blocks.ENCHANTING_TABLE.getDefaultState()).setHardness(15f);
        ((AbstractBlockStateAccessor) Blocks.SPAWNER.getDefaultState()).setLuminance(4);

        DefaultItemComponentEvents.MODIFY.register(modifyContext -> {
            modifyContext.modify(item -> item instanceof PotionItem, (builder, item) -> {
                builder.add(DataComponentTypes.MAX_STACK_SIZE, 4);
            });
            modifyContext.modify(MEATS::contains, (builder, item) -> {
                builder.add(DataComponentTypes.MAX_STACK_SIZE, 16);
            });
            modifyContext.modify(PIES::contains, (builder, item) -> {
                builder.add(DataComponentTypes.MAX_STACK_SIZE, 1);
            });

            modifyContext.modify(NETHERITE_EQUIPMENT::contains, (builder, item) -> builder.add(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "netherite"));
            modifyContext.modify(DIAMOND_EQUIPMENT::contains, (builder, item) -> builder.add(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "diamond"));
            modifyContext.modify(IRON_EQUIPMENT::contains, (builder, item) -> builder.add(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "iron"));
            modifyContext.modify(GOLD_EQUIPMENT::contains, (builder, item) -> builder.add(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "gold"));
            modifyContext.modify(COPPER_EQUIPMENT::contains, (builder, item) -> builder.add(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "copper"));
            modifyContext.modify(STONE_EQUIPMENT::contains, (builder, item) -> builder.add(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "stone"));
            modifyContext.modify(WOOD_EQUIPMENT::contains, (builder, item) -> builder.add(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "wood"));
            modifyContext.modify(LEATHER_EQUIPMENT::contains, (builder, item) -> builder.add(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "leather"));
            modifyContext.modify(CHAIN_EQUIPMENT::contains, (builder, item) -> builder.add(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "chain"));
            modifyContext.modify(item -> item == Items.TURTLE_HELMET, (builder, item) -> builder.add(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "turtle_scute"));
            modifyContext.modify(item -> item == Items.WOLF_ARMOR, (builder, item) -> builder.add(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "armadillo_scute"));
        });

        ElementHolder.register(Enchantments.FIRE_ASPECT, Element.FIRE);
        ElementHolder.register(Enchantments.FIRE_PROTECTION, Element.FIRE);
        ElementHolder.register(Enchantments.FLAME, Element.FIRE);

        ElementHolder.register(Enchantments.DENSITY, Element.EARTH);
        ElementHolder.register(Enchantments.SILK_TOUCH, Element.EARTH);
        ElementHolder.register(Enchantments.EFFICIENCY, Element.EARTH);
        ElementHolder.register(Enchantments.BREACH, Element.EARTH);
        ElementHolder.register(Enchantments.THORNS, Element.EARTH);
        ElementHolder.register(Enchantments.UNBREAKING, Element.EARTH);

        ElementHolder.register(Enchantments.WIND_BURST, Element.AIR);
        ElementHolder.register(Enchantments.PROJECTILE_PROTECTION, Element.AIR);
        ElementHolder.register(Enchantments.FEATHER_FALLING, Element.AIR);
        ElementHolder.register(Enchantments.SWEEPING_EDGE, Element.AIR);
        ElementHolder.register(Enchantments.KNOCKBACK, Element.AIR);
        ElementHolder.register(Enchantments.PUNCH, Element.AIR);

        ElementHolder.register(Enchantments.LOYALTY, Element.LIGHTNING);
        ElementHolder.register(Enchantments.CHANNELING, Element.LIGHTNING);

        ElementHolder.register(Enchantments.DEPTH_STRIDER, Element.WATER);
        ElementHolder.register(Enchantments.AQUA_AFFINITY, Element.WATER);
        ElementHolder.register(Enchantments.RESPIRATION, Element.WATER);
        ElementHolder.register(Enchantments.IMPALING, Element.WATER);
        ElementHolder.register(Enchantments.RIPTIDE, Element.WATER);
        ElementHolder.register(Enchantments.BLAST_PROTECTION, Element.WATER);
        ElementHolder.register(Enchantments.LUCK_OF_THE_SEA, Element.WATER);

        ElementHolder.register(Enchantments.FROST_WALKER, Element.ICE);
        ElementHolder.register(Enchantments.PIERCING, Element.ICE);
        ElementHolder.register(Enchantments.MULTISHOT, Element.ICE);
        ElementHolder.register(Enchantments.QUICK_CHARGE, Element.ICE);

        ElementHolder.register(Enchantments.SMITE, Element.LIGHT);
        ElementHolder.register(Enchantments.BANE_OF_ARTHROPODS, Element.LIGHT);
        ElementHolder.register(Enchantments.FORTUNE, Element.LIGHT);
        ElementHolder.register(Enchantments.BINDING_CURSE, Element.LIGHT);
        ElementHolder.register(Enchantments.PROTECTION, Element.LIGHT);

        ElementHolder.register(Enchantments.SWIFT_SNEAK, Element.DARK);
        ElementHolder.register(Enchantments.SOUL_SPEED, Element.DARK);
        ElementHolder.register(Enchantments.LURE, Element.DARK);
        ElementHolder.register(Enchantments.VANISHING_CURSE, Element.DARK);
        ElementHolder.register(Enchantments.SHARPNESS, Element.DARK);
        ElementHolder.register(Enchantments.POWER, Element.DARK);

        ElementHolder.register(Enchantments.INFINITY, Element.VOID);

        ElementHolder.register(Enchantments.MENDING, Element.AETHER);
        ElementHolder.register(Enchantments.LOOTING, Element.AETHER);
        ElementHolder.register(Enchantments.FORTUNE, Element.AETHER);
    }
}
