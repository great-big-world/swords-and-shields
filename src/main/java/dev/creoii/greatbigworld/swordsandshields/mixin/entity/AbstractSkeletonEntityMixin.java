package dev.creoii.greatbigworld.swordsandshields.mixin.entity;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin extends HostileEntity {
    @Shadow public abstract void updateAttackType();
    @Unique private static final TrackedData<Byte> ARROWS = DataTracker.registerData(AbstractSkeletonEntity.class, TrackedDataHandlerRegistry.BYTE);

    protected AbstractSkeletonEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ARROWS, (byte) 32);
    }

    @Inject(method = "createAbstractSkeletonAttributes", at = @At("RETURN"), cancellable = true)
    private static void gbw$weakerSkeletons(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue().add(EntityAttributes.MAX_HEALTH, 16));
    }

    @Inject(method = "initialize", at = @At("RETURN"))
    private void gbw$initRandomArrowCount(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        dataTracker.set(ARROWS, (byte) (32 - random.nextInt(24)));
    }

    @Inject(method = "shootAt", at = @At("TAIL"))
    private void gbw$subtractArrow(LivingEntity target, float pullProgress, CallbackInfo ci) {
        dataTracker.set(ARROWS, (byte) (dataTracker.get(ARROWS) - 1));
        if (dataTracker.get(ARROWS) <= 0) {
            setStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW), ItemStack.EMPTY);
            updateAttackType();
        }
    }
}
