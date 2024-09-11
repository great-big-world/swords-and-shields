package dev.creoii.greatbigworld.swordsandshields.mixin.client;

import com.mojang.authlib.GameProfile;
import dev.creoii.greatbigworld.swordsandshields.util.ExtendedPlayer;
import dev.creoii.greatbigworld.swordsandshields.util.SwordsAndShieldsTags;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntity implements ExtendedPlayer {
    @Unique private static final int DEFAULT_HIDE_STATUS_HUD_TIME = 200;
    @Unique private int gbw$shouldHideHealthHud = DEFAULT_HIDE_STATUS_HUD_TIME;
    @Unique private int gbw$shouldHideArmorHud = DEFAULT_HIDE_STATUS_HUD_TIME;
    @Unique private int gbw$shouldHideFoodHud = DEFAULT_HIDE_STATUS_HUD_TIME;
    @Unique private int gbw$shouldHideExpHud = DEFAULT_HIDE_STATUS_HUD_TIME;

    public ClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift = At.Shift.AFTER))
    private void gbw$hideHudIfNotInWater(CallbackInfo ci) {
        if (getStatusEffects().stream().noneMatch(instance -> instance.getEffectType().isIn(SwordsAndShieldsTags.PRESERVES_HEALTH_HUD)) && !isSubmergedInWater()) {
            if (--gbw$shouldHideHealthHud < 0)
                gbw$shouldHideHealthHud = -1;
        } else if (getHealth() <= 6) {
            gbw$resetHideHealthHud();
        } else gbw$resetHideHealthHud();

        if (getStatusEffects().stream().noneMatch(instance -> instance.getEffectType().isIn(SwordsAndShieldsTags.PRESERVES_ARMOR_HUD))) {
            if (--gbw$shouldHideArmorHud < 0)
                gbw$shouldHideArmorHud = -1;
        }  else if (getArmor() <= 6) {
            gbw$resetHideArmorHud();
        } else gbw$resetHideArmorHud();

        if (getStatusEffects().stream().noneMatch(instance -> instance.getEffectType().isIn(SwordsAndShieldsTags.PRESERVES_FOOD_HUD))) {
            if (--gbw$shouldHideFoodHud < 0)
                gbw$shouldHideFoodHud = -1;
        } else if (getHungerManager().getFoodLevel() <= 6) {
            gbw$resetHideFoodHud();
        } else gbw$resetHideFoodHud();

        if (getStatusEffects().stream().noneMatch(instance -> instance.getEffectType().isIn(SwordsAndShieldsTags.PRESERVES_EXPERIENCE_HUD))) {
            if (--gbw$shouldHideExpHud < 0)
                gbw$shouldHideExpHud = -1;
        } else gbw$resetHideExpHud();
    }

    @Override
    public int gbw$getHideHealthHud() {
        return gbw$shouldHideHealthHud;
    }

    @Override
    public void gbw$resetHideHealthHud() {
        gbw$shouldHideHealthHud = DEFAULT_HIDE_STATUS_HUD_TIME;
    }

    @Override
    public int gbw$getHideArmorHud() {
        return gbw$shouldHideArmorHud;
    }

    @Override
    public void gbw$resetHideArmorHud() {
        gbw$shouldHideArmorHud = DEFAULT_HIDE_STATUS_HUD_TIME;
    }

    @Override
    public int gbw$getHideFoodHud() {
        return gbw$shouldHideFoodHud;
    }

    @Override
    public void gbw$resetHideFoodHud() {
        gbw$shouldHideFoodHud = DEFAULT_HIDE_STATUS_HUD_TIME;
    }

    @Override
    public int gbw$getHideExpHud() {
        return gbw$shouldHideExpHud;
    }

    @Override
    public void gbw$resetHideExpHud() {
        gbw$shouldHideExpHud = DEFAULT_HIDE_STATUS_HUD_TIME;
    }
}
