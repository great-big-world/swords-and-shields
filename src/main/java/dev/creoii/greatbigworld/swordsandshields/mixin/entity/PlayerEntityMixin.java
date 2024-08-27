package dev.creoii.greatbigworld.swordsandshields.mixin.entity;

import dev.creoii.greatbigworld.swordsandshields.util.ExtendedPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;eatFood(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"))
    private void gbw$showHudFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (world.isClient && this instanceof ExtendedPlayer extendedPlayer) {
            extendedPlayer.gbw$resetHideFoodHud();
        }
    }
}
