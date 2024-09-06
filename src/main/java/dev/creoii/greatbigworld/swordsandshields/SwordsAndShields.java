package dev.creoii.greatbigworld.swordsandshields;

import dev.creoii.creoapi.api.event.entity.LivingEntityEvents;
import dev.creoii.greatbigworld.swordsandshields.util.ExtendedPlayer;
import net.fabricmc.api.ModInitializer;

public class SwordsAndShields implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";

    @Override
    public void onInitialize() {
        LivingEntityEvents.EQUIP_STACK.register((livingEntity, slot, oldStack, newStack) -> {
            if (livingEntity instanceof ExtendedPlayer extendedPlayer) {
                extendedPlayer.gbw$resetHideStatusHud();
            }
            return true;
        });
    }
}
