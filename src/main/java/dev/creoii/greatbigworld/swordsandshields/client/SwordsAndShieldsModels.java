package dev.creoii.greatbigworld.swordsandshields.client;

import dev.creoii.greatbigworld.event.ModelEvents;
import dev.creoii.greatbigworld.swordsandshields.client.model.GoatHornModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public final class SwordsAndShieldsModels {
    public static ModelLayerLocation GOAT_HORN_DECORATION;

    public static void register() {
        GOAT_HORN_DECORATION = ModelEvents.registerModelLayer(Identifier.withDefaultNamespace("player"), "goat_horn");

        ModelEvents.REGISTER_LAYER_DEFINITION.register(builder -> {
            builder.put(GOAT_HORN_DECORATION, GoatHornModel.createGoatHornLayer());
        });
    }
}
