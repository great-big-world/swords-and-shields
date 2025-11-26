package dev.creoii.greatbigworld.swordsandshields.mixin.entity;

import dev.creoii.greatbigworld.swordsandshields.util.ExtendedScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "onScreenHandlerOpened", at = @At("TAIL"))
    private void gbw$setScreenHandlerPlayer(ScreenHandler screenHandler, CallbackInfo ci) {
        if (screenHandler instanceof ExtendedScreenHandler extendedScreenHandler) {
            extendedScreenHandler.gbw$setPlayer((PlayerEntity) (Object) this);
        }
    }
}
