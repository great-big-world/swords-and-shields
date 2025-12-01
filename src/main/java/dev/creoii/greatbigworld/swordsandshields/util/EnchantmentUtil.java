package dev.creoii.greatbigworld.swordsandshields.util;

import dev.creoii.greatbigworld.element.Element;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;

public final class EnchantmentUtil {
    public static boolean canBeCombined(RegistryEntry<Enchantment> first, RegistryEntry<Enchantment> second) {
        Element a = ElementHolder.gbw$getElement(first.getKey().orElseThrow());
        Element b = ElementHolder.gbw$getElement(first.getKey().orElseThrow());

        if (a != null && b != null && (a == b || a.getSynergy() == null || a.getSynergy() == b || b.getSynergy() == null || b.getSynergy() == a)) {
            return !first.equals(second) && !first.value().exclusiveSet().contains(second) && !second.value().exclusiveSet().contains(first);
        }

        return false;
    }
}
