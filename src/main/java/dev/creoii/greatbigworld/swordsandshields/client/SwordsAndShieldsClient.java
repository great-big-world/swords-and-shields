package dev.creoii.greatbigworld.swordsandshields.client;

import dev.creoii.greatbigworld.swordsandshields.util.ExtendedLivingEntity;
import dev.creoii.greatbigworld.swordsandshields.util.StruckByLightningS2C;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.UUID;

public class SwordsAndShieldsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(StruckByLightningS2C.PACKET_ID, (struckByLightningS2C, context) -> {
            UUID uuid = struckByLightningS2C.uuid();
            context.client().execute(() -> {
                if (context.client().world.getEntity(uuid) instanceof ExtendedLivingEntity extendedLivingEntity) {
                    extendedLivingEntity.gbw$setStruckByLightningTime(10);
                }
            });
        });
    }
}
