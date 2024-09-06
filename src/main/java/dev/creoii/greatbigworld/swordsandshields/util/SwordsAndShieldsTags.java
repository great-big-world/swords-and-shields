package dev.creoii.greatbigworld.swordsandshields.util;

import dev.creoii.greatbigworld.swordsandshields.SwordsAndShields;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class SwordsAndShieldsTags {
    public static final TagKey<StatusEffect> PRESERVES_HEALTH_HUD = TagKey.of(RegistryKeys.STATUS_EFFECT, new Identifier(SwordsAndShields.NAMESPACE, "preserves_health_hud"));
    public static final TagKey<StatusEffect> PRESERVES_ARMOR_HUD = TagKey.of(RegistryKeys.STATUS_EFFECT, new Identifier(SwordsAndShields.NAMESPACE, "preserves_armor_hud"));
    public static final TagKey<StatusEffect> PRESERVES_FOOD_HUD = TagKey.of(RegistryKeys.STATUS_EFFECT, new Identifier(SwordsAndShields.NAMESPACE, "preserves_food_hud"));
    public static final TagKey<StatusEffect> PRESERVES_EXPERIENCE_HUD = TagKey.of(RegistryKeys.STATUS_EFFECT, new Identifier(SwordsAndShields.NAMESPACE, "preserves_experience_hud"));
}
