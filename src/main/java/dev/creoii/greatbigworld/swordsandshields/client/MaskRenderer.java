package dev.creoii.greatbigworld.swordsandshields.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.swordsandshields.client.model.MaskModel;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

@Environment(EnvType.CLIENT)
public record MaskRenderer(MaskModel maskModel) implements ArmorRenderer {
    public static final Map<Item, Identifier> TEXTURES = ImmutableMap.<Item, Identifier>builder()
            .put(SwordsAndShieldsItems.OAK_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/oak.png"))
            .put(SwordsAndShieldsItems.SPRUCE_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/spruce.png"))
            .put(SwordsAndShieldsItems.BIRCH_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/birch.png"))
            .put(SwordsAndShieldsItems.JUNGLE_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/jungle.png"))
            .put(SwordsAndShieldsItems.ACACIA_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/acacia.png"))
            .put(SwordsAndShieldsItems.DARK_OAK_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/dark_oak.png"))
            .put(SwordsAndShieldsItems.MANGROVE_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/mangrove.png"))
            .put(SwordsAndShieldsItems.CHERRY_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/cherry.png"))
            .put(SwordsAndShieldsItems.BAMBOO_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/bamboo.png"))
            .put(SwordsAndShieldsItems.ASPEN_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/aspen.png"))
            .put(SwordsAndShieldsItems.ACAI_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/acai.png"))
            .put(SwordsAndShieldsItems.WARPED_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/warped.png"))
            .put(SwordsAndShieldsItems.CRIMSON_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/crimson.png"))
            .put(SwordsAndShieldsItems.LEATHER_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/leather.png"))
            .put(SwordsAndShieldsItems.CHAINMAIL_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/chainmail.png"))
            .put(SwordsAndShieldsItems.COPPER_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/copper.png"))
            .put(SwordsAndShieldsItems.IRON_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/iron.png"))
            .put(SwordsAndShieldsItems.GOLDEN_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/golden.png"))
            .put(SwordsAndShieldsItems.DIAMOND_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/diamond.png"))
            .put(SwordsAndShieldsItems.NETHERITE_MASK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/models/armor/mask/netherite.png"))
            .build();

    @Override
    public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack stack, HumanoidRenderState humanoidRenderState, EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> model) {
        OrderedSubmitNodeCollector orderedSubmitNodeCollector = submitNodeCollector.order(0);
        ArmorRenderer.submitTransformCopyingModel(model, humanoidRenderState, maskModel, humanoidRenderState, true, orderedSubmitNodeCollector, poseStack, RenderTypes.armorCutoutNoCull(TEXTURES.get(stack.getItem())), light, OverlayTexture.NO_OVERLAY, humanoidRenderState.outlineColor, null);

        if (stack.hasFoil()) {
            ArmorRenderer.submitTransformCopyingModel(model, humanoidRenderState, maskModel, humanoidRenderState, true, orderedSubmitNodeCollector, poseStack, RenderTypes.armorEntityGlint(), light, OverlayTexture.NO_OVERLAY, humanoidRenderState.outlineColor, null);
        }
    }
}