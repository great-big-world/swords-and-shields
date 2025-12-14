package dev.creoii.greatbigworld.swordsandshields.mixin.client.render;

import dev.creoii.greatbigworld.swordsandshields.client.layer.GoatHornLayer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.monster.piglin.ZombifiedPiglinModel;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.ZombifiedPiglinRenderer;
import net.minecraft.client.renderer.entity.state.ZombifiedPiglinRenderState;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombifiedPiglinRenderer.class)
public abstract class ZombifiedPiglinRendererMixin extends HumanoidMobRenderer<ZombifiedPiglin, ZombifiedPiglinRenderState, ZombifiedPiglinModel> {
    public ZombifiedPiglinRendererMixin(EntityRendererProvider.Context context, ZombifiedPiglinModel humanoidModel, float f) {
        super(context, humanoidModel, f);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$addDecorationLayers(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation, ModelLayerLocation modelLayerLocation2, ArmorModelSet armorModelSet, ArmorModelSet armorModelSet2, CallbackInfo ci) {
        addLayer(new GoatHornLayer<>(this, context));
    }
}
