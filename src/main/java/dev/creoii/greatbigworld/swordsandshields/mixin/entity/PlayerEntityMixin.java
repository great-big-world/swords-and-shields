package dev.creoii.greatbigworld.swordsandshields.mixin.entity;

import dev.creoii.greatbigworld.swordsandshields.util.ExtendedPlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;eatFood(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"))
    private void gbw$showHudFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (world.isClient && this instanceof ExtendedPlayer extendedPlayer) {
            extendedPlayer.gbw$resetHideFoodHud();
        }
    }

    @ModifyConstant(method = "getXpToDrop", constant = @Constant(intValue = 7))
    private int gbw$dropMoreXp(int constant) {
        return 12;
    }

    @ModifyConstant(method = "getXpToDrop", constant = @Constant(intValue = 100))
    private int gbw$allowMoreXpToDrop(int constant) {
        return 170;
    }
}
