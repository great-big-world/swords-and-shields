package dev.creoii.greatbigworld.swordsandshields.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public final class SwordsAndShieldsDamageTypes {
    public static final ResourceKey<DamageType> BAMBOO = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "bamboo"));
}
