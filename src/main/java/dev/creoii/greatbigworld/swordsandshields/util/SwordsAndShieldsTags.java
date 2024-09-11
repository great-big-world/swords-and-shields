package dev.creoii.greatbigworld.swordsandshields.util;

import dev.creoii.greatbigworld.swordsandshields.SwordsAndShields;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class SwordsAndShieldsTags {
    public static final TagKey<Item> HINTS_HEALTH_HUD = TagKey.of(RegistryKeys.ITEM, new Identifier(SwordsAndShields.NAMESPACE, "hints_health_hud"));
    public static final TagKey<Item> HINTS_ARMOR_HUD = TagKey.of(RegistryKeys.ITEM, new Identifier(SwordsAndShields.NAMESPACE, "hints_armor_hud"));
    public static final TagKey<Item> HINTS_FOOD_HUD = TagKey.of(RegistryKeys.ITEM, new Identifier(SwordsAndShields.NAMESPACE, "hints_food_hud"));
    public static final TagKey<Item> HINTS_EXPERIENCE_HUD = TagKey.of(RegistryKeys.ITEM, new Identifier(SwordsAndShields.NAMESPACE, "hints_experience_hud"));

    public static final TagKey<StatusEffect> PRESERVES_HEALTH_HUD = TagKey.of(RegistryKeys.STATUS_EFFECT, new Identifier(SwordsAndShields.NAMESPACE, "preserves_health_hud"));
    public static final TagKey<StatusEffect> PRESERVES_ARMOR_HUD = TagKey.of(RegistryKeys.STATUS_EFFECT, new Identifier(SwordsAndShields.NAMESPACE, "preserves_armor_hud"));
    public static final TagKey<StatusEffect> PRESERVES_FOOD_HUD = TagKey.of(RegistryKeys.STATUS_EFFECT, new Identifier(SwordsAndShields.NAMESPACE, "preserves_food_hud"));
    public static final TagKey<StatusEffect> PRESERVES_EXPERIENCE_HUD = TagKey.of(RegistryKeys.STATUS_EFFECT, new Identifier(SwordsAndShields.NAMESPACE, "preserves_experience_hud"));
}
