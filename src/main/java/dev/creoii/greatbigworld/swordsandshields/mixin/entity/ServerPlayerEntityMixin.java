package dev.creoii.greatbigworld.swordsandshields.mixin.entity;

import dev.creoii.greatbigworld.swordsandshields.util.ExtendedScreenHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "initMenu", at = @At("TAIL"))
    private void gbw$setScreenHandlerPlayer(AbstractContainerMenu screenHandler, CallbackInfo ci) {
        if (screenHandler instanceof ExtendedScreenHandler extendedScreenHandler) {
            extendedScreenHandler.gbw$setPlayer((Player) (Object) this);
        }
    }
}
