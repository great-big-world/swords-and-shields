package dev.creoii.greatbigworld.swordsandshields.client;

import dev.creoii.greatbigworld.swordsandshields.util.ExtendedPlayer;
import dev.creoii.greatbigworld.swordsandshields.util.SyncStatusHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class SwordsAndShieldsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SyncStatusHud.PACKET_ID, (payload, context) -> {
            boolean health = payload.health();
            boolean food = payload.food();
            boolean armor = payload.armor();
            boolean experience = payload.experience();
            context.client().execute(() -> {
                if (context.player() instanceof ExtendedPlayer extendedPlayer) {
                    if (health)
                        extendedPlayer.gbw$resetHideStatusHud();
                    if (food)
                        extendedPlayer.gbw$resetHideFoodHud();
                    if (experience)
                        extendedPlayer.gbw$resetHideExpHud();
                }
            });
        });
    }
}
