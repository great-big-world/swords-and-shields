package dev.creoii.greatbigworld.swordsandshields.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public final class SwordsAndShieldsDamageTypes {
    public static final RegistryKey<DamageType> BAMBOO = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "bamboo"));
}
