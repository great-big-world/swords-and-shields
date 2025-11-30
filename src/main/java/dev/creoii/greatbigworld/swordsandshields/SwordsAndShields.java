package dev.creoii.greatbigworld.swordsandshields;

import com.google.common.collect.ImmutableList;
import dev.creoii.greatbigworld.element.Element;
import dev.creoii.greatbigworld.mixin.AbstractBlockStateAccessor;
import dev.creoii.greatbigworld.swordsandshields.item.SwordsAndShieldsArmorMaterials;
import dev.creoii.greatbigworld.swordsandshields.item.SwordsAndShieldsToolMaterials;
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

    @Override
    public void onInitialize() {
        SwordsAndShieldsArmorMaterials.register();
        SwordsAndShieldsToolMaterials.register();

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
