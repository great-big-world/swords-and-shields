package dev.creoii.greatbigworld.swordsandshields.client;

import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsScreens;
import net.fabricmc.api.ClientModInitializer;

public class SwordsAndShieldsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SwordsAndShieldsModels.register();
        SwordsAndShieldsScreens.registerClient();
    }
}
