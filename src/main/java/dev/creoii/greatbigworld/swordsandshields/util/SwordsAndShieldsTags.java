package dev.creoii.greatbigworld.swordsandshields.util;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

public final class SwordsAndShieldsTags {
    public static final TagKey<TrimMaterial> DECORATION = TagKey.create(Registries.TRIM_MATERIAL, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "decoration"));
}
