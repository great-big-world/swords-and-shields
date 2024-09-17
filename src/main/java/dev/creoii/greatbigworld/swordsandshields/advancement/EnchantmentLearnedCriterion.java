package dev.creoii.greatbigworld.swordsandshields.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsCriteria;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class EnchantmentLearnedCriterion extends AbstractCriterion<EnchantmentLearnedCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, Identifier enchantment) {
        trigger(player, conditions -> conditions.matches(enchantment));
    }

    public record Conditions(Optional<LootContextPredicate> player, Identifier enchantment) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player), Identifier.CODEC.fieldOf("enchantment").forGetter(Conditions::enchantment)).apply(instance, Conditions::new);
        });

        public static AdvancementCriterion<Conditions> create(Identifier enchantment) {
            return SwordsAndShieldsCriteria.ENCHANTMENT_LEARNED.create(new Conditions(Optional.empty(), enchantment));
        }

        private boolean matches(Identifier enchantment) {
            return enchantment.equals(enchantment());
        }
    }
}
