package dev.creoii.greatbigworld.swordsandshields.util;

import dev.creoii.greatbigworld.element.Element;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public interface ElementHolder {
    Map<RegistryKey<Enchantment>, Element> ENCHANTMENT_ENTRIES = new HashMap<>();

    @Nullable
    static Element gbw$getElement(RegistryKey<Enchantment> key) {
        return ENCHANTMENT_ENTRIES.getOrDefault(key, null);
    }

    static ElementHolder of(Enchantment enchantment) {
        return (ElementHolder) (Object) enchantment;
    }

    static void register(RegistryKey<Enchantment> enchantment, Element element) {
        ENCHANTMENT_ENTRIES.put(enchantment, element);
    }
}
