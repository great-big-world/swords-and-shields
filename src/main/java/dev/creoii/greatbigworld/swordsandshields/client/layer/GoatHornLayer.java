package dev.creoii.greatbigworld.swordsandshields.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.swordsandshields.client.SwordsAndShieldsModels;
import dev.creoii.greatbigworld.swordsandshields.client.model.GoatHornModel;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsTrimMaterials;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;

@Environment(EnvType.CLIENT)
public class GoatHornLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> extends RenderLayer<S, M> {
    private static final Identifier GOAT_HORN_LOCATION = Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/entity/armor_decoration/goat_horn.png");
    private final HumanoidModel<HumanoidRenderState> model;

    public GoatHornLayer(RenderLayerParent<S, M> renderLayerParent, EntityRendererProvider.Context context) {
        super(renderLayerParent);
        this.model = new GoatHornModel(context.getModelSet().bakeLayer(SwordsAndShieldsModels.GOAT_HORN_DECORATION));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, HumanoidRenderState entityRenderState, float f, float g) {
        ItemStack head = entityRenderState.headEquipment;
        if (head.has(DataComponents.TRIM) && !entityRenderState.isInvisible) {
            ArmorTrim armorTrim = head.get(DataComponents.TRIM);
            if (armorTrim.material().is(SwordsAndShieldsTrimMaterials.GOAT_HORN)) {
                int j = LivingEntityRenderer.getOverlayCoords(entityRenderState, 0f);
                submitNodeCollector.submitModel(model, entityRenderState, poseStack, RenderTypes.entitySolid(GOAT_HORN_LOCATION), i, j, entityRenderState.outlineColor, null);
            }
        }
    }
}
