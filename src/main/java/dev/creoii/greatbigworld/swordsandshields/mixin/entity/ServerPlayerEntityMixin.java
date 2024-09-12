package dev.creoii.greatbigworld.swordsandshields.mixin.entity;

import dev.creoii.greatbigworld.swordsandshields.util.SyncStatusHud;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "changeGameMode", at = @At("RETURN"))
    private void gbw$resetStatusHudOnSurvival(GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
        if (gameMode == GameMode.SURVIVAL) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) (Object) this;
            ServerPlayNetworking.send(serverPlayer, new SyncStatusHud(SyncStatusHud.Type.FOOD));
            ServerPlayNetworking.send(serverPlayer, new SyncStatusHud(SyncStatusHud.Type.HEALTH));
            ServerPlayNetworking.send(serverPlayer, new SyncStatusHud(SyncStatusHud.Type.ARMOR));
            ServerPlayNetworking.send(serverPlayer, new SyncStatusHud(SyncStatusHud.Type.EXPERIENCE));
        }
    }
}
