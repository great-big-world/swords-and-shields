package dev.creoii.greatbigworld.swordsandshields.client;

import dev.creoii.greatbigworld.swordsandshields.client.model.GoatHornModel;
import dev.creoii.greatbigworld.swordsandshields.client.model.MaskModel;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsItems;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public final class SwordsAndShieldsModels {
    public static final ModelLayerLocation GOAT_HORN_DECORATION = new ModelLayerLocation(Identifier.withDefaultNamespace("player"), "goat_horn");
    public static final ModelLayerLocation MASK = new ModelLayerLocation(Identifier.withDefaultNamespace("player"), "mask");

    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(GOAT_HORN_DECORATION, GoatHornModel::createGoatHornLayer);
        EntityModelLayerRegistry.registerModelLayer(MASK, MaskModel::createMaskLayer);

        ArmorRenderer.register(context -> new MaskRenderer(new MaskModel(context.getModelSet().bakeLayer(SwordsAndShieldsModels.MASK))),
                SwordsAndShieldsItems.OAK_MASK, SwordsAndShieldsItems.SPRUCE_MASK, SwordsAndShieldsItems.BIRCH_MASK, SwordsAndShieldsItems.JUNGLE_MASK,
                SwordsAndShieldsItems.ACACIA_MASK, SwordsAndShieldsItems.DARK_OAK_MASK, SwordsAndShieldsItems.MANGROVE_MASK, SwordsAndShieldsItems.CHERRY_MASK,
                SwordsAndShieldsItems.BAMBOO_MASK, SwordsAndShieldsItems.ACAI_MASK, SwordsAndShieldsItems.ASPEN_MASK,
                SwordsAndShieldsItems.CRIMSON_MASK, SwordsAndShieldsItems.WARPED_MASK,
                SwordsAndShieldsItems.LEATHER_MASK, SwordsAndShieldsItems.CHAINMAIL_MASK,
                SwordsAndShieldsItems.COPPER_MASK, SwordsAndShieldsItems.IRON_MASK, SwordsAndShieldsItems.GOLDEN_MASK,
                SwordsAndShieldsItems.DIAMOND_MASK, SwordsAndShieldsItems.NETHERITE_MASK
        );
    }
}
