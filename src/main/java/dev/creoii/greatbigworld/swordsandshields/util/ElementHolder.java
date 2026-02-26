package dev.creoii.greatbigworld.swordsandshields.util;

import dev.creoii.greatbigworld.element.Element;
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
}
