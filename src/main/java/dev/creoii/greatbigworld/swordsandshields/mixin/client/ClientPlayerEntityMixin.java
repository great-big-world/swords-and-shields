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
    @Unique private int gbw$shouldHideStatusHud = DEFAULT_HIDE_STATUS_HUD_TIME;
    @Unique private int gbw$shouldHideFoodHud = DEFAULT_HIDE_STATUS_HUD_TIME;
    @Unique private int gbw$shouldHideExpHud = DEFAULT_HIDE_STATUS_HUD_TIME;

    public ClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift = At.Shift.AFTER))
    private void gbw$hideHudIfNotInWater(CallbackInfo ci) {
        if (getStatusEffects().stream().noneMatch(instance -> instance.getEffectType().isIn(SwordsAndShieldsTags.PRESERVES_HEALTH_HUD) || instance.getEffectType().isIn(SwordsAndShieldsTags.PRESERVES_ARMOR_HUD)) && !isSubmergedInWater()) {
            if (--gbw$shouldHideStatusHud < 0)
                gbw$shouldHideStatusHud = -1;
        } else gbw$resetHideStatusHud();

        if (getStatusEffects().stream().noneMatch(instance -> instance.getEffectType().isIn(SwordsAndShieldsTags.PRESERVES_FOOD_HUD))) {
            if (--gbw$shouldHideFoodHud < 0)
                gbw$shouldHideFoodHud = -1;
        } else gbw$resetHideStatusHud();

        if (getStatusEffects().stream().noneMatch(instance -> instance.getEffectType().isIn(SwordsAndShieldsTags.PRESERVES_EXPERIENCE_HUD))) {
            if (--gbw$shouldHideExpHud < 0)
                gbw$shouldHideExpHud = -1;
        } else gbw$resetHideStatusHud();
    }

    @Inject(method = "setExperience", at = @At("TAIL"))
    private void gbw$showHudExp(float progress, int total, int level, CallbackInfo ci) {
        gbw$resetHideExpHud();
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);
        gbw$resetHideStatusHud();
    }

    @Override
    public int gbw$getHideStatusHud() {
        return gbw$shouldHideStatusHud;
    }

    @Override
    public void gbw$resetHideStatusHud() {
        gbw$shouldHideStatusHud = DEFAULT_HIDE_STATUS_HUD_TIME;
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
