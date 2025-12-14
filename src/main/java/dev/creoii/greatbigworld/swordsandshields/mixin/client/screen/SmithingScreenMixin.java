package dev.creoii.greatbigworld.swordsandshields.mixin.client.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin extends ItemCombinerScreen<SmithingMenu> {
    @Mutable @Shadow @Final private CyclingSlotBackground templateIcon;
    @Mutable @Shadow @Final private CyclingSlotBackground baseIcon;
    @Mutable @Shadow @Final private CyclingSlotBackground additionalIcon;

    public SmithingScreenMixin(SmithingMenu itemCombinerMenu, Inventory inventory, Component component, Identifier identifier) {
        super(itemCombinerMenu, inventory, component, identifier);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$moveTitleX(SmithingMenu smithingMenu, Inventory inventory, Component component, CallbackInfo ci) {
        titleLabelX += 32;

        templateIcon = null;
        baseIcon = new CyclingSlotBackground(0);
        additionalIcon = new CyclingSlotBackground(1);
    }

    @ModifyConstant(method = "renderErrorIcon", constant = @Constant(intValue = 65))
    private int gbw$moveErrorIconX(int constant) {
        return constant + 34;
    }

    @ModifyConstant(method = "renderErrorIcon", constant = @Constant(intValue = 46))
    private int gbw$moveErrorIconY(int constant) {
        return constant - 1;
    }

    @Inject(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;submitEntityRenderState(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;FLorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;IIII)V"), cancellable = true)
    private void gbw$cancelRenderArmorStand0(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "slotChanged", at = @At("HEAD"), cancellable = true)
    private void gbw$cancelUpdateArmorStand(AbstractContainerMenu abstractContainerMenu, int i, ItemStack itemStack, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "subInit", at = @At("HEAD"), cancellable = true)
    private void gbw$cancelUpdateArmorStandInit(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/SmithingScreen;renderOnboardingTooltips(Lnet/minecraft/client/gui/GuiGraphics;II)V"), cancellable = true)
    private void gbw$cancelRenderStupidTooltips(CallbackInfo ci) {
        ci.cancel();
    }

    @WrapOperation(method = "containerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CyclingSlotBackground;tick(Ljava/util/List;)V", ordinal = 0))
    private void gbw$cancelTemplateSlotTick(CyclingSlotBackground instance, List<Identifier> list, Operation<Void> original) {
    }

    @WrapOperation(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CyclingSlotBackground;render(Lnet/minecraft/world/inventory/AbstractContainerMenu;Lnet/minecraft/client/gui/GuiGraphics;FII)V", ordinal = 0))
    private void gbw$cancelTemplateSlotTick(CyclingSlotBackground instance, AbstractContainerMenu abstractContainerMenu, GuiGraphics guiGraphics, float f, int i, int j, Operation<Void> original) {
    }
}
