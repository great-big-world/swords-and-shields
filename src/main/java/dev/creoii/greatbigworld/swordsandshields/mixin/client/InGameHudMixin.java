package dev.creoii.greatbigworld.swordsandshields.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.swordsandshields.util.ExtendedPlayer;
import dev.creoii.greatbigworld.swordsandshields.util.SwordsAndShieldsTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHealthBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V"))
    private void gbw$hideHealthBarPre(DrawContext context, CallbackInfo ci, @Local PlayerEntity player) {
        if (player instanceof ExtendedPlayer extendedPlayer) {
            if (extendedPlayer.gbw$getHideHealthHud() <= 0 && player.getStackInHand(player.getActiveHand()).isIn(SwordsAndShieldsTags.HINTS_HEALTH_HUD)) {
                // sin wave for opacity
                float alpha = MathHelper.sin(.2f * client.world.getTime()) * .25f + .25f;
                context.setShaderColor(1f, 1f, 1f,  alpha);
            } else if (extendedPlayer.gbw$getHideHealthHud() < 20)
                context.setShaderColor(1f, 1f, 1f, extendedPlayer.gbw$getHideHealthHud() / 20f);
        }
    }

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;getRiddenEntity()Lnet/minecraft/entity/LivingEntity;"))
    private void gbw$hideHealthBarPost(DrawContext context, CallbackInfo ci, @Local PlayerEntity player) {
        if (player instanceof ExtendedPlayer extendedPlayer && extendedPlayer.gbw$getHideHealthHud() < 20) {
            context.setShaderColor(1f, 1f, 1f, 1f);
        }
    }

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderArmor(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;IIII)V"))
    private void gbw$hideArmorBarPre(DrawContext context, CallbackInfo ci, @Local PlayerEntity player) {
        if (player instanceof ExtendedPlayer extendedPlayer) {
            if (extendedPlayer.gbw$getHideArmorHud() <= 0 && player.getStackInHand(Hand.MAIN_HAND).isIn(SwordsAndShieldsTags.HINTS_ARMOR_HUD)) {
                float alpha = MathHelper.sin(.2f * client.world.getTime()) * .25f + .25f;
                context.setShaderColor(1f, 1f, 1f,  alpha);
            } else if (extendedPlayer.gbw$getHideArmorHud() < 20)
                context.setShaderColor(1f, 1f, 1f, extendedPlayer.gbw$getHideArmorHud() / 20f);
        }
    }

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 0))
    private void gbw$hideArmorBarPost(DrawContext context, CallbackInfo ci, @Local PlayerEntity player) {
        if (player instanceof ExtendedPlayer extendedPlayer && extendedPlayer.gbw$getHideArmorHud() < 20) {
            context.setShaderColor(1f, 1f, 1f, 1f);
        }
    }

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderFood(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;II)V"))
    private void gbw$hideFoodBarPre(DrawContext context, CallbackInfo ci, @Local PlayerEntity player) {
        if (player instanceof ExtendedPlayer extendedPlayer) {
            if (extendedPlayer.gbw$getHideFoodHud() <= 0 && player.getStackInHand(Hand.MAIN_HAND).isIn(SwordsAndShieldsTags.HINTS_FOOD_HUD)) {
                float alpha = MathHelper.sin(.2f * client.world.getTime()) * .25f + .25f;
                context.setShaderColor(1f, 1f, 1f,  alpha);
            } else if (extendedPlayer.gbw$getHideFoodHud() < 20)
                context.setShaderColor(1f, 1f, 1f, extendedPlayer.gbw$getHideFoodHud() / 20f);
        }
    }

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 2))
    private void gbw$hideFoodBarPost(DrawContext context, CallbackInfo ci, @Local PlayerEntity player) {
        if (player instanceof ExtendedPlayer extendedPlayer && extendedPlayer.gbw$getHideFoodHud() < 20) {
            context.setShaderColor(1f, 1f, 1f, 1f);
        }
    }

    @Inject(method = "renderMainHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderExperienceBar(Lnet/minecraft/client/gui/DrawContext;I)V"))
    private void gbw$hideExpBarPre(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (client.player instanceof ExtendedPlayer extendedPlayer) {
            if (extendedPlayer.gbw$getHideExpHud() <= 0 && client.player.getStackInHand(Hand.MAIN_HAND).isIn(SwordsAndShieldsTags.HINTS_EXPERIENCE_HUD)) {
                float alpha = MathHelper.sin(.2f * client.world.getTime()) * .25f + .25f;
                context.setShaderColor(1f, 1f, 1f,  alpha);
            } else if (extendedPlayer.gbw$getHideExpHud() < 20)
                context.setShaderColor(1f, 1f, 1f, extendedPlayer.gbw$getHideExpHud() / 20f);
        }
    }

    @Inject(method = "renderMainHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasStatusBars()Z"))
    private void gbw$hideExpBarPost(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (client.player instanceof ExtendedPlayer extendedPlayer && extendedPlayer.gbw$getHideExpHud() < 20) {
            context.setShaderColor(1f, 1f, 1f, 1f);
        }
    }

    @Inject(method = "renderExperienceLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void gbw$hideExpLevelPre(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (client.player instanceof ExtendedPlayer extendedPlayer) {
            if (extendedPlayer.gbw$getHideExpHud() <= 0 && client.player.getStackInHand(Hand.MAIN_HAND).isIn(SwordsAndShieldsTags.HINTS_EXPERIENCE_HUD)) {
                float alpha = MathHelper.sin(.2f * client.world.getTime()) * .25f + .25f;
                context.setShaderColor(1f, 1f, 1f,  alpha);
            } else if (extendedPlayer.gbw$getHideExpHud() < 20)
                context.setShaderColor(1f, 1f, 1f, extendedPlayer.gbw$getHideExpHud() / 20f);
        }
    }

    @Inject(method = "renderExperienceLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V"))
    private void gbw$hideExpLevelPost(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (client.player instanceof ExtendedPlayer extendedPlayer && extendedPlayer.gbw$getHideExpHud() < 20) {
            context.setShaderColor(1f, 1f, 1f, 1f);
        }
    }

    // group Positioning
    @Redirect(method = "renderFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    private void gbw$repositionFoodForHiddenExp(DrawContext instance, Identifier texture, int x, int y, int width, int height, @Local(argsOnly = true) PlayerEntity player) {
        if (player instanceof ExtendedPlayer extendedPlayer && extendedPlayer.gbw$getHideExpHud() < 0) {
            instance.drawGuiTexture(texture, x, y + 7, width, height);
        } else instance.drawGuiTexture(texture, x, y, width, height);
    }

    @Redirect(method = "renderHealthBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawHeart(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/gui/hud/InGameHud$HeartType;IIZZZ)V"))
    private void gbw$repositionHealthForHiddenExp(InGameHud instance, DrawContext context, InGameHud.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half, @Local(argsOnly = true) PlayerEntity player) {
        if (player instanceof ExtendedPlayer extendedPlayer && extendedPlayer.gbw$getHideExpHud() < 0) {
            instance.drawHeart(context, type, x, y + 7, hardcore, blinking, half);
        } else instance.drawHeart(context, type, x, y, hardcore, blinking, half);
    }

    @Redirect(method = "renderArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    private static void gbw$repositionArmorForHiddenExp(DrawContext instance, Identifier texture, int x, int y, int width, int height, @Local(argsOnly = true) PlayerEntity player) {
        int offset = 0;
        if (player instanceof ExtendedPlayer extendedPlayer) {
            if (extendedPlayer.gbw$getHideExpHud() < 0)
                offset += 7;
            if (extendedPlayer.gbw$getHideHealthHud() < 0)
                offset += 10;
        }

        instance.drawGuiTexture(texture, x, y + offset, width, height);
    }

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    private void gbw$repositionBubblesForHiddenExp(DrawContext instance, Identifier texture, int x, int y, int width, int height, @Local PlayerEntity playerEntity) {
        int offset = 0;
        if (playerEntity instanceof ExtendedPlayer extendedPlayer) {
            if (extendedPlayer.gbw$getHideExpHud() < 0)
                offset += 7;
            if (extendedPlayer.gbw$getHideFoodHud() < 0)
                offset += 10;
        }
        instance.drawGuiTexture(texture, x, y + offset, width, height);
    }

    @Redirect(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"))
    private int gbw$repositionHeldItemNameForHiddenHud(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int color) {
        if (client.player instanceof ExtendedPlayer extendedPlayer) {
            int offset = 0;
            if (extendedPlayer.gbw$getHideExpHud() < 0 && extendedPlayer.gbw$getHideFoodHud() < 0)
                offset += 7;
            if (extendedPlayer.gbw$getHideHealthHud() < 0 && extendedPlayer.gbw$getHideArmorHud() < 0)
                offset += 10;
            return instance.drawTextWithShadow(textRenderer, text, x, y + offset, color);
        }
       return instance.drawTextWithShadow(textRenderer, text, x, y, color);
    }
    // endgroup
}
