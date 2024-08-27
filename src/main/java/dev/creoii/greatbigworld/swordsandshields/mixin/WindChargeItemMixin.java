package dev.creoii.greatbigworld.swordsandshields.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WindChargeItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WindChargeItem.class)
public class WindChargeItemMixin {
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    private void gbw$applyWindChargeElytraBoosting(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (user.isFallFlying()) {
            Vec3d rotVec2 = user.getRotationVector().negate();
            Vec3d current = user.getVelocity();

            double d = 1.5d;
            double e = .1d;
            double y = rotVec2.y * e + (rotVec2.y * d - current.y) * .5d;
            user.setVelocity(current.add(rotVec2.x * e + (rotVec2.x * d - current.x) * .125d, Math.clamp(y, -.2675d, .2675d), rotVec2.z * e + (rotVec2.z * d - current.z) * .125d));
        }
    }

    @ModifyConstant(method = "use", constant = @Constant(intValue = 10))
    private int gbw$shortenCooldownWhenFlying(int constant, @Local(argsOnly = true) PlayerEntity user) {
        return user.isFallFlying() ? 2 : constant;
    }
}
