package dev.creoii.greatbigworld.swordsandshields.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> {
    @Shadow @Final private static Identifier ERROR_SPRITE;

    public AnvilScreenMixin(AnvilMenu itemCombinerMenu, Inventory inventory, Component component, Identifier identifier) {
        super(itemCombinerMenu, inventory, component, identifier);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$offsetInventoryLabelY(AnvilMenu anvilMenu, Inventory inventory, Component component, CallbackInfo ci) {
        inventoryLabelY += 31;
        imageHeight = 197;
    }

    @Inject(method = "renderLabels", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AnvilMenu;getCost()I"), cancellable = true)
    private void gbw$dontRenderEnchantmentCost(GuiGraphics guiGraphics, int i, int j, CallbackInfo ci) {
        ci.cancel();
    }

    @WrapOperation(method = "renderErrorIcon", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"))
    private void gbw$adjustErrorIcon(GuiGraphics instance, RenderPipeline renderPipeline, Identifier identifier, int i, int j, int k, int l, Operation<Void> original) {
        instance.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, i - 63, j + 19, 15, 16);
    }
}
