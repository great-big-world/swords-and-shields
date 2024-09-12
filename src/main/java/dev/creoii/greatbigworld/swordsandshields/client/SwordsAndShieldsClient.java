package dev.creoii.greatbigworld.swordsandshields.client;

import dev.creoii.greatbigworld.swordsandshields.util.ExtendedPlayer;
import dev.creoii.greatbigworld.swordsandshields.util.SyncStatusHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class SwordsAndShieldsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SyncStatusHud.PACKET_ID, (payload, context) -> {
            SyncStatusHud.Type type = payload.type();
            context.client().execute(() -> {
                if (context.player() instanceof ExtendedPlayer extendedPlayer) {
                    switch (type) {
                        case FOOD -> extendedPlayer.gbw$resetHideFoodHud();
                        case ARMOR -> extendedPlayer.gbw$resetHideArmorHud();
                        case HEALTH -> extendedPlayer.gbw$resetHideHealthHud();
                        case EXPERIENCE -> extendedPlayer.gbw$resetHideExpHud();
                    }
                }
            });
        });
    }
}
