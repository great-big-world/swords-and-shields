package dev.creoii.greatbigworld.swordsandshields;

import dev.creoii.creoapi.api.event.entity.LivingEntityEvents;
import dev.creoii.greatbigworld.swordsandshields.util.ExtendedPlayer;
import dev.creoii.greatbigworld.swordsandshields.util.SyncStatusHud;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.ActionResult;

public class SwordsAndShields implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(SyncStatusHud.PACKET_ID, SyncStatusHud.PACKET_CODEC);

        LivingEntityEvents.EQUIP_STACK.register((livingEntity, slot, oldStack, newStack) -> {
            if (livingEntity instanceof ExtendedPlayer extendedPlayer) {
                extendedPlayer.gbw$resetHideStatusHud();
            }
            return true;
        });
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClient && player instanceof ExtendedPlayer extendedPlayer) {
                extendedPlayer.gbw$resetHideStatusHud();
            }
            return ActionResult.PASS;
        });
    }
}
