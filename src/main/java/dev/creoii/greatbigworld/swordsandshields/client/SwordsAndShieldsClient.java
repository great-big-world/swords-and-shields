package dev.creoii.greatbigworld.swordsandshields.client;

import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldBlocks;
import dev.creoii.greatbigworld.swordsandshields.util.DynamicHudPlayer;
import dev.creoii.greatbigworld.swordsandshields.util.SyncStatusHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class SwordsAndShieldsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SwordsAndShieldBlocks.registerClient();

        ClientPlayNetworking.registerGlobalReceiver(SyncStatusHud.PACKET_ID, (payload, context) -> {
            SyncStatusHud.Type type = payload.type();
            context.client().execute(() -> {
                if (context.player() instanceof DynamicHudPlayer dynamicHudPlayer) {
                    switch (type) {
                        case FOOD -> dynamicHudPlayer.gbw$resetHideFoodHud();
                        case ARMOR -> dynamicHudPlayer.gbw$resetHideArmorHud();
                        case HEALTH -> dynamicHudPlayer.gbw$resetHideHealthHud();
                        case EXPERIENCE -> dynamicHudPlayer.gbw$resetHideExpHud();
                    }
                }
            });
        });
    }
}
