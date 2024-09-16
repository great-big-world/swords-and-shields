package dev.creoii.greatbigworld.swordsandshields.util;

import net.minecraft.enchantment.Enchantment;

import java.util.Set;

public interface EnchantmentPlayer {
    void gbw$addEnchantment(Enchantment enchantment);

    Set<Enchantment> gbw$getEnchantments();
}
