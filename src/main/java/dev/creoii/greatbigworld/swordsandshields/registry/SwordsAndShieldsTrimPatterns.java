package dev.creoii.greatbigworld.swordsandshields.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.TrimPattern;

public final class SwordsAndShieldsTrimPatterns {
    public static final ResourceKey<TrimPattern> NONE = ResourceKey.create(Registries.TRIM_PATTERN, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "none"));
}
