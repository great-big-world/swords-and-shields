package dev.creoii.greatbigworld.swordsandshields.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

@Environment(EnvType.CLIENT)
public class GoatHornModel extends HumanoidModel<HumanoidRenderState> {
    public GoatHornModel(ModelPart modelPart) {
        super(modelPart);
    }

    public static LayerDefinition createGoatHornLayer() {
        MeshDefinition meshDefinition = PlayerModel.createMesh(CubeDeformation.NONE, false);
        PartDefinition partDefinition = meshDefinition.getRoot().clearRecursively();
        PartDefinition partDefinition2 = partDefinition.getChild("head");
        partDefinition2.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(8, 0).addBox(0f, -1f, -1f, 3f, 2f, 2f).mirror(false).texOffs(0, 0).mirror().addBox(1f, -5f, -1f, 2f, 4f, 2f).mirror(false), PartPose.offset(5f, -6.5f, 0f));
        partDefinition2.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(8, 0).addBox(-3f, -1f, -1f, 3f, 2f, 2f).texOffs(0, 0).addBox(-3f, -5f, -1f, 2f, 4f, 2f), PartPose.offset(-5f, -6.5f, 0f));
        return LayerDefinition.create(meshDefinition, 32, 16);
    }

    public void setupAnim(HumanoidRenderState humanoidRenderState) {
        super.setupAnim(humanoidRenderState);

        if (humanoidRenderState instanceof ArmorStandRenderState armorStandRenderState) {
            head.xRot = (float) (Math.PI / 180f) * armorStandRenderState.headPose.x();
            head.yRot = (float) (Math.PI / 180f) * armorStandRenderState.headPose.y();
            head.zRot = (float) (Math.PI / 180f) * armorStandRenderState.headPose.z();
        }
    }
}
