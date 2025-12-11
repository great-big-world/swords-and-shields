package dev.creoii.greatbigworld.swordsandshields.mixin.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSkeleton.class)
public abstract class AbstractSkeletonEntityMixin extends Monster {
    @Shadow public abstract void reassessWeaponGoal();
    @Unique private static final EntityDataAccessor<Byte> ARROWS = SynchedEntityData.defineId(AbstractSkeleton.class, EntityDataSerializers.BYTE);

    protected AbstractSkeletonEntityMixin(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ARROWS, (byte) 32);
    }

    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void gbw$weakerSkeletons(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue().add(Attributes.MAX_HEALTH, 16));
    }

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void gbw$initRandomArrowCount(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData entityData, CallbackInfoReturnable<SpawnGroupData> cir) {
        this.entityData.set(ARROWS, (byte) (32 - random.nextInt(24)));
    }

    @Inject(method = "performRangedAttack", at = @At("TAIL"))
    private void gbw$subtractArrow(LivingEntity target, float pullProgress, CallbackInfo ci) {
        entityData.set(ARROWS, (byte) (entityData.get(ARROWS) - 1));
        if (entityData.get(ARROWS) <= 0) {
            setItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW), ItemStack.EMPTY);
            reassessWeaponGoal();
        }
    }
}
