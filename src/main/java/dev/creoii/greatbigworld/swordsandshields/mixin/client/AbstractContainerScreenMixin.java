package dev.creoii.greatbigworld.swordsandshields.mixin.client;

import dev.creoii.greatbigworld.swordsandshields.util.ExtendedScreenHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin<T extends AbstractContainerMenu> {
    @Shadow
    @Final
    protected T menu;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$initClientPlayer(AbstractContainerMenu abstractContainerMenu, Inventory inventory, Component component, CallbackInfo ci) {
        if (menu instanceof ExtendedScreenHandler extendedScreenHandler) {
            extendedScreenHandler.gbw$setPlayer(Minecraft.getInstance().player);
        }
    }
}
