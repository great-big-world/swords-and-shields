package dev.creoii.greatbigworld.swordsandshields.mixin.client.render;

import dev.creoii.greatbigworld.swordsandshields.client.layer.GoatHornLayer;
import net.minecraft.client.model.monster.skeleton.SkeletonModel;
import net.minecraft.client.renderer.entity.AbstractSkeletonRenderer;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonRenderer.class)
public abstract class AbstractSkeletonRendererMixin<T extends AbstractSkeleton, S extends SkeletonRenderState> extends HumanoidMobRenderer<T, S, SkeletonModel<S>> {
    public AbstractSkeletonRendererMixin(EntityRendererProvider.Context context, SkeletonModel<S> humanoidModel, float f) {
        super(context, humanoidModel, f);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Lnet/minecraft/client/renderer/entity/ArmorModelSet;Lnet/minecraft/client/model/monster/skeleton/SkeletonModel;)V", at = @At("TAIL"))
    private void gbw$addDecorationLayers(EntityRendererProvider.Context context, ArmorModelSet armorModelSet, SkeletonModel skeletonModel, CallbackInfo ci) {
        addLayer(new GoatHornLayer<>(this, context));
    }
}
