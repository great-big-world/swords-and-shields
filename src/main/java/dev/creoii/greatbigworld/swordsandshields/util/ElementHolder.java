package dev.creoii.greatbigworld.swordsandshields.util;

import dev.creoii.greatbigworld.element.Element;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public interface ElementHolder {
    Map<ResourceKey<Enchantment>, Element> ENCHANTMENT_ENTRIES = new HashMap<>();

    @Nullable
    static Element gbw$getElement(ResourceKey<Enchantment> key) {
        return ENCHANTMENT_ENTRIES.getOrDefault(key, null);
    }

    static ElementHolder of(Enchantment enchantment) {
        return (ElementHolder) (Object) enchantment;
    }

    static void register(ResourceKey<Enchantment> enchantment, Element element) {
        if (ENCHANTMENT_ENTRIES.containsKey(enchantment)) {
            System.out.println("Attempted to associate enchantment " + enchantment.identifier() + " with multiple elements");
            return;
        }
        ENCHANTMENT_ENTRIES.put(enchantment, element);
    }

    static void register() {
        register(Enchantments.FIRE_ASPECT, Element.FIRE);
        register(Enchantments.FIRE_PROTECTION, Element.FIRE);
        register(Enchantments.FLAME, Element.FIRE);

        register(Enchantments.DENSITY, Element.EARTH);
        register(Enchantments.SILK_TOUCH, Element.EARTH);
        register(Enchantments.EFFICIENCY, Element.EARTH);
        register(Enchantments.BREACH, Element.EARTH);
        register(Enchantments.THORNS, Element.EARTH);
        register(Enchantments.UNBREAKING, Element.EARTH);

        register(Enchantments.WIND_BURST, Element.AIR);
        register(Enchantments.PROJECTILE_PROTECTION, Element.AIR);
        register(Enchantments.FEATHER_FALLING, Element.AIR);
        register(Enchantments.SWEEPING_EDGE, Element.AIR);
        register(Enchantments.KNOCKBACK, Element.AIR);
        register(Enchantments.PUNCH, Element.AIR);

        register(Enchantments.LOYALTY, Element.LIGHTNING);
        register(Enchantments.CHANNELING, Element.LIGHTNING);
        register(Enchantments.LUNGE, Element.LIGHTNING);

        register(Enchantments.DEPTH_STRIDER, Element.WATER);
        register(Enchantments.AQUA_AFFINITY, Element.WATER);
        register(Enchantments.RESPIRATION, Element.WATER);
        register(Enchantments.IMPALING, Element.WATER);
        register(Enchantments.RIPTIDE, Element.WATER);
        register(Enchantments.BLAST_PROTECTION, Element.WATER);
        register(Enchantments.LUCK_OF_THE_SEA, Element.WATER);

        register(Enchantments.FROST_WALKER, Element.ICE);
        register(Enchantments.PIERCING, Element.ICE);
        register(Enchantments.MULTISHOT, Element.ICE);
        register(Enchantments.QUICK_CHARGE, Element.ICE);

        register(Enchantments.SMITE, Element.LIGHT);
        register(Enchantments.BANE_OF_ARTHROPODS, Element.LIGHT);
        register(Enchantments.FORTUNE, Element.LIGHT);
        register(Enchantments.BINDING_CURSE, Element.LIGHT);
        register(Enchantments.PROTECTION, Element.LIGHT);
        register(Enchantments.LOOTING, Element.AETHER);

        register(Enchantments.SWIFT_SNEAK, Element.DARK);
        register(Enchantments.SOUL_SPEED, Element.DARK);
        register(Enchantments.LURE, Element.DARK);
        register(Enchantments.VANISHING_CURSE, Element.DARK);
        register(Enchantments.SHARPNESS, Element.DARK);
        register(Enchantments.POWER, Element.DARK);

        register(Enchantments.INFINITY, Element.VOID);

        register(Enchantments.MENDING, Element.AETHER);
    }
}
