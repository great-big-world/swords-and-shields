package dev.creoii.greatbigworld.swordsandshields;

import com.google.common.collect.ImmutableList;
import dev.creoii.greatbigworld.element.Element;
import dev.creoii.greatbigworld.mixin.AbstractBlockStateAccessor;
import dev.creoii.greatbigworld.swordsandshields.item.SwordsAndShieldsArmorMaterials;
import dev.creoii.greatbigworld.swordsandshields.item.SwordsAndShieldsToolMaterials;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsDataComponentTypes;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsItems;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsRecipes;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsTrimMaterials;
import dev.creoii.greatbigworld.swordsandshields.util.ElementHolder;
import dev.creoii.greatbigworld.swordsandshields.util.EquipmentUpgrading;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.component.ProvidesTrimMaterial;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
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
            .add(Items.NETHERITE_SPEAR)
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
            .add(Items.DIAMOND_SPEAR)
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
            .add(Items.GOLDEN_SPEAR)
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
            .add(Items.IRON_SPEAR)
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
            .add(Items.COPPER_SPEAR)
            .build();
    private static final List<Item> STONE_EQUIPMENT = new ImmutableList.Builder<Item>()
            .add(Items.STONE_PICKAXE)
            .add(Items.STONE_SWORD)
            .add(Items.STONE_AXE)
            .add(Items.STONE_SHOVEL)
            .add(Items.STONE_HOE)
            .add(Items.STONE_SPEAR)
            .build();
    private static final List<Item> WOOD_EQUIPMENT = new ImmutableList.Builder<Item>()
            .add(Items.WOODEN_PICKAXE)
            .add(Items.WOODEN_SWORD)
            .add(Items.WOODEN_AXE)
            .add(Items.WOODEN_SHOVEL)
            .add(Items.WOODEN_HOE)
            .add(Items.WOODEN_SPEAR)
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
        SwordsAndShieldsItems.register();
        SwordsAndShieldsArmorMaterials.register();
        SwordsAndShieldsToolMaterials.register();
        SwordsAndShieldsDataComponentTypes.register();
        SwordsAndShieldsRecipes.register();
        EquipmentUpgrading.register();

        ((AbstractBlockStateAccessor) Blocks.ENCHANTING_TABLE.defaultBlockState()).setDestroySpeed(15f);
        ((AbstractBlockStateAccessor) Blocks.SPAWNER.defaultBlockState()).setLightEmission(4);

        DefaultItemComponentEvents.MODIFY.register(modifyContext -> {
            modifyContext.modify(item -> item instanceof PotionItem, (builder, item) -> {
                builder.set(DataComponents.MAX_STACK_SIZE, 4);
            });
            modifyContext.modify(MEATS::contains, (builder, item) -> {
                builder.set(DataComponents.MAX_STACK_SIZE, 16);
            });
            modifyContext.modify(PIES::contains, (builder, item) -> {
                builder.set(DataComponents.MAX_STACK_SIZE, 1);
            });

            modifyContext.modify(NETHERITE_EQUIPMENT::contains, (builder, item) -> {
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "netherite");
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES, 0);
            });
            modifyContext.modify(DIAMOND_EQUIPMENT::contains, (builder, item) -> {
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "diamond");
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES, 0);
            });
            modifyContext.modify(IRON_EQUIPMENT::contains, (builder, item) -> {
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "iron");
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES, 0);
            });
            modifyContext.modify(GOLD_EQUIPMENT::contains, (builder, item) -> {
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "gold");
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES, 0);
            });
            modifyContext.modify(COPPER_EQUIPMENT::contains, (builder, item) -> {
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "copper");
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES, 0);
            });
            modifyContext.modify(STONE_EQUIPMENT::contains, (builder, item) -> {
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "stone");
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES, 0);
            });
            modifyContext.modify(WOOD_EQUIPMENT::contains, (builder, item) -> {
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "wood");
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES, 0);
            });
            modifyContext.modify(LEATHER_EQUIPMENT::contains, (builder, item) -> {
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "leather");
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES, 0);
            });
            modifyContext.modify(CHAIN_EQUIPMENT::contains, (builder, item) -> {
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "chain");
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES, 0);
            });
            modifyContext.modify(item -> item == Items.TURTLE_HELMET, (builder, item) -> {
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "turtle_scute");
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES, 0);
            });
            modifyContext.modify(item -> item == Items.WOLF_ARMOR, (builder, item) -> {
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL, "armadillo_scute");
                builder.set(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES, 0);
            });

            modifyContext.modify(item -> item == Items.BROWN_DYE, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.BROWN))));
            modifyContext.modify(item -> item == Items.RED_DYE, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.RED))));
            modifyContext.modify(item -> item == Items.ORANGE_DYE, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.ORANGE))));
            modifyContext.modify(item -> item == Items.YELLOW_DYE, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.YELLOW))));
            modifyContext.modify(item -> item == Items.LIME_DYE, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.LIME))));
            modifyContext.modify(item -> item == Items.GREEN_DYE, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.GREEN))));
            modifyContext.modify(item -> item == Items.CYAN_DYE, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.CYAN))));
            modifyContext.modify(item -> item == Items.BLUE_DYE, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.BLUE))));
            modifyContext.modify(item -> item == Items.LIGHT_BLUE_DYE, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.LIGHT_BLUE))));
            modifyContext.modify(item -> item == Items.PINK_DYE, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.PINK))));
            modifyContext.modify(item -> item == Items.MAGENTA_DYE, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.MAGENTA))));
            modifyContext.modify(item -> item == Items.PURPLE_DYE, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.PURPLE))));
            modifyContext.modify(item -> item == Items.BLACK_DYE, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.BLACK))));
            modifyContext.modify(item -> item == Items.GRAY_DYE, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.GRAY))));
            modifyContext.modify(item -> item == Items.LIGHT_GRAY_DYE, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.LIGHT_GRAY))));
            modifyContext.modify(item -> item == Items.WHITE_DYE, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.WHITE))));
            modifyContext.modify(item -> item == Items.GOAT_HORN, (builder, item) -> builder.set(DataComponents.PROVIDES_TRIM_MATERIAL, new ProvidesTrimMaterial(new EitherHolder<>(SwordsAndShieldsTrimMaterials.GOAT_HORN))));
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
        ElementHolder.register(Enchantments.LUNGE, Element.LIGHTNING);

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
        ElementHolder.register(Enchantments.LOOTING, Element.AETHER);

        ElementHolder.register(Enchantments.SWIFT_SNEAK, Element.DARK);
        ElementHolder.register(Enchantments.SOUL_SPEED, Element.DARK);
        ElementHolder.register(Enchantments.LURE, Element.DARK);
        ElementHolder.register(Enchantments.VANISHING_CURSE, Element.DARK);
        ElementHolder.register(Enchantments.SHARPNESS, Element.DARK);
        ElementHolder.register(Enchantments.POWER, Element.DARK);

        ElementHolder.register(Enchantments.INFINITY, Element.VOID);

        ElementHolder.register(Enchantments.MENDING, Element.AETHER);
    }
}
