package dev.creoii.greatbigworld.swordsandshields.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.swordsandshields.SwordsAndShields;
import dev.creoii.greatbigworld.swordsandshields.advancement.EnchantmentLearnedCriterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class SwordsAndShieldsCriteria {
    public static final EnchantmentLearnedCriterion ENCHANTMENT_LEARNED = new EnchantmentLearnedCriterion();

    public static void register() {
        Registry.register(Registries.CRITERION, new Identifier(GreatBigWorld.NAMESPACE, "enchantment_learned"), ENCHANTMENT_LEARNED);
    }
}
