package dev.creoii.greatbigworld.swordsandshields.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class MaskModel extends EntityModel<HumanoidRenderState> {
	public final ModelPart mask;

	public MaskModel(ModelPart root) {
        super(root);
        mask = root.getChild("mask");
	}

	@Override
	public void setupAnim(HumanoidRenderState humanoidRenderState) {
		resetPose();

		float f = humanoidRenderState.swimAmount;
		boolean bl = humanoidRenderState.isFallFlying;

		mask.xRot = humanoidRenderState.xRot * ((float)Math.PI / 180f);
		mask.yRot = humanoidRenderState.yRot * ((float)Math.PI / 180f);

		if (bl) mask.xRot = -.7853982f;
		else if (f > 0f) mask.xRot = Mth.rotLerpRad(f, mask.xRot, -.7853982f);

		if (humanoidRenderState.isCrouching) mask.y += 4.2f;
	}

	public static LayerDefinition createMaskLayer() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		modelPartData.addOrReplaceChild("mask", CubeListBuilder.create().texOffs(0, 18).addBox(-5f, -9f, -5f, 10f, 10f, 4f)
				.texOffs(0, 0).addBox(-9f, -13f, -3f, 18f, 18f, 0f), PartPose.ZERO);
		return LayerDefinition.create(modelData, 64, 32);
	}
}