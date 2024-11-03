package dev.creoii.greatbigworld.swordsandshields.mixin.entity;

import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentPlayer;
import dev.creoii.greatbigworld.swordsandshields.util.SyncStatusHud;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements EnchantmentPlayer {
    @Unique private final Set<RegistryKey<Enchantment>> enchantments = new HashSet<>();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    public boolean gbw$addEnchantment(RegistryKey<Enchantment> enchantment) {
        return enchantments.add(enchantment);
    }

    public Set<RegistryKey<Enchantment>> gbw$getEnchantments() {
        return enchantments;
    }

    @Inject(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;eatFood(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/component/type/FoodComponent;)Lnet/minecraft/item/ItemStack;"))
    private void gbw$showHudFood(World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> cir) {
        if (!world.isClient) {
            if ((PlayerEntity) (Object) this instanceof ServerPlayerEntity serverPlayer && serverPlayer.interactionManager != null && serverPlayer.networkHandler != null) {
                ServerPlayNetworking.send(serverPlayer, new SyncStatusHud(SyncStatusHud.Type.FOOD));
            }
        }
    }

    @Inject(method = "addExperience", at = @At("TAIL"))
    private void gbw$showHudExp(int experience, CallbackInfo ci) {
        if (!getWorld().isClient) {
            if ((PlayerEntity) (Object) this instanceof ServerPlayerEntity serverPlayer && serverPlayer.interactionManager != null && serverPlayer.networkHandler != null) {
                ServerPlayNetworking.send(serverPlayer, new SyncStatusHud(SyncStatusHud.Type.EXPERIENCE));
            }
        }
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);
        if (!getWorld().isClient) {
            if ((PlayerEntity) (Object) this instanceof ServerPlayerEntity serverPlayer && serverPlayer.interactionManager != null && serverPlayer.networkHandler != null) {
                ServerPlayNetworking.send(serverPlayer, new SyncStatusHud(SyncStatusHud.Type.HEALTH));
            }
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
