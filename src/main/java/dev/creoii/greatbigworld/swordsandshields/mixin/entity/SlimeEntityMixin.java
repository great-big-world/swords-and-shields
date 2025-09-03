package dev.creoii.greatbigworld.swordsandshields.mixin.entity;

import com.google.common.collect.Maps;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.conversion.EntityConversionContext;
import net.minecraft.entity.conversion.EntityConversionType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin extends MobEntity {
    @Shadow public abstract int getSize();

    protected SlimeEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void gbw$mergeSlimes(CallbackInfo ci) {
        if (!getWorld().isClient && getVelocity().lengthSquared() > .275d && isAlive() && !hasCustomName()) {
            List<SlimeEntity> list = getWorld().getEntitiesByClass(SlimeEntity.class, getBoundingBox().expand(0.20000000298023224d, -0.009999999776482582d, 0.20000000298023224d), entity -> entity != (Object) this);
            if (!list.isEmpty()) {
                MutableInt maxSize = new MutableInt(getSize());
                MutableFloat totalHealth = new MutableFloat(getHealth());
                Map<RegistryEntry<StatusEffect>, Integer> effectCounts = Maps.newHashMap();

                for (SlimeEntity slimeEntity : list) {
                    if (Math.abs(getSize() - slimeEntity.getSize()) > 2)
                        continue;

                    if (slimeEntity.getSize() > maxSize.getValue())
                        maxSize.setValue(slimeEntity.getSize());
                    totalHealth.add(slimeEntity.getHealth());

                    slimeEntity.getStatusEffects().forEach(statusEffectInstance -> {
                        if (effectCounts.containsKey(statusEffectInstance.getEffectType())) {
                            effectCounts.put(statusEffectInstance.getEffectType(), effectCounts.get(statusEffectInstance.getEffectType()) + 1);
                        } else {
                            effectCounts.put(statusEffectInstance.getEffectType(), 1);
                        }
                    });

                    slimeEntity.discard();
                }

                convertTo(EntityType.SLIME, new EntityConversionContext(EntityConversionType.SPLIT_ON_DEATH, false, false, getScoreboardTeam()), SpawnReason.TRIGGERED, newSlime -> {
                    newSlime.setSize(maxSize.getValue() + 1, false);
                    newSlime.setHealth(totalHealth.getValue() / list.size() + 1);
                    newSlime.refreshPositionAndAngles(getPos(), getYaw(), getPitch());
                    newSlime.setVelocity(getVelocity());
                    newSlime.velocityDirty = true;
                });

                playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1f, (random.nextFloat() - random.nextFloat()) * .2f + 1f);
                discard();
            }
        }
    }
}
