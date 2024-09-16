package dev.creoii.greatbigworld.swordsandshields;

import dev.creoii.creoapi.api.event.entity.LivingEntityEvents;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldBlocks;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldItems;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsBlockEntities;
import dev.creoii.greatbigworld.swordsandshields.util.SyncStatusHud;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class SwordsAndShields implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";

    @Override
    public void onInitialize() {
        SwordsAndShieldBlocks.register();
        SwordsAndShieldsBlockEntities.register();
        SwordsAndShieldItems.register();

        PayloadTypeRegistry.playS2C().register(SyncStatusHud.PACKET_ID, SyncStatusHud.PACKET_CODEC);

        LivingEntityEvents.EQUIP_STACK.register((livingEntity, slot, oldStack, newStack) -> {
            if (!livingEntity.getWorld().isClient) {
                if (livingEntity instanceof ServerPlayerEntity serverPlayer && serverPlayer.interactionManager != null && serverPlayer.networkHandler != null) {
                    ServerPlayNetworking.send(serverPlayer, new SyncStatusHud(SyncStatusHud.Type.ARMOR));
                }
            }
            return true;
        });
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient) {
                if (player instanceof ServerPlayerEntity serverPlayer && serverPlayer.interactionManager != null && serverPlayer.networkHandler != null) {
                    ServerPlayNetworking.send(serverPlayer, new SyncStatusHud(SyncStatusHud.Type.HEALTH));
                }
            }
            return ActionResult.PASS;
        });
    }
}
