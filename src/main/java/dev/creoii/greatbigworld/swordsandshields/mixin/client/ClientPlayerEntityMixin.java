package dev.creoii.greatbigworld.swordsandshields.mixin.client;

import com.mojang.authlib.GameProfile;
import dev.creoii.greatbigworld.swordsandshields.util.DynamicHudPlayer;
import dev.creoii.greatbigworld.swordsandshields.util.SwordsAndShieldsTags;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements DynamicHudPlayer {
    @Unique private static final int DEFAULT_HIDE_STATUS_HUD_TIME = 200;
    @Unique private int gbw$shouldHideHealthHud = DEFAULT_HIDE_STATUS_HUD_TIME;
    @Unique private int gbw$shouldHideArmorHud = DEFAULT_HIDE_STATUS_HUD_TIME;
    @Unique private int gbw$shouldHideFoodHud = DEFAULT_HIDE_STATUS_HUD_TIME;
    @Unique private int gbw$shouldHideExpHud = DEFAULT_HIDE_STATUS_HUD_TIME;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift = At.Shift.AFTER))
    private void gbw$tickHideStatusHuds(CallbackInfo ci) {
        if (!isCreative() && !isSpectator()) {
            if (getHealth() >= getMaxHealth() && getStatusEffects().stream().noneMatch(instance -> instance.getEffectType().isIn(SwordsAndShieldsTags.PRESERVES_HEALTH_HUD)) && getHealth() > 6 && !isSubmergedInWater()) {
                if (--gbw$shouldHideHealthHud < 0)
                    gbw$shouldHideHealthHud = -1;
            } else gbw$resetHideHealthHud();

            if (getStatusEffects().stream().noneMatch(instance -> instance.getEffectType().isIn(SwordsAndShieldsTags.PRESERVES_ARMOR_HUD)) && getArmor() > 6) {
                if (--gbw$shouldHideArmorHud < 0)
                    gbw$shouldHideArmorHud = -1;
            } else gbw$resetHideArmorHud();

            if (getHungerManager().getFoodLevel() >= 20 && getStatusEffects().stream().noneMatch(instance -> instance.getEffectType().isIn(SwordsAndShieldsTags.PRESERVES_FOOD_HUD)) && getHungerManager().getFoodLevel() > 6) {
                if (--gbw$shouldHideFoodHud < 0)
                    gbw$shouldHideFoodHud = -1;
            } else gbw$resetHideFoodHud();

            if (getStatusEffects().stream().noneMatch(instance -> instance.getEffectType().isIn(SwordsAndShieldsTags.PRESERVES_EXPERIENCE_HUD))) {
                if (--gbw$shouldHideExpHud < 0)
                    gbw$shouldHideExpHud = -1;
            } else gbw$resetHideExpHud();

            /*System.out.println("health: " + gbw$shouldHideHealthHud);
            System.out.println("armor: " + gbw$shouldHideArmorHud);
            System.out.println("food: " + gbw$shouldHideFoodHud);
            System.out.println("exp: " + gbw$shouldHideExpHud);*/
        }
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
