package dev.creoii.greatbigworld.swordsandshields.mixin.client.render;

import dev.creoii.greatbigworld.swordsandshields.client.layer.GoatHornLayer;
import net.minecraft.client.model.monster.zombie.ZombieVillagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.ZombieVillagerRenderer;
import net.minecraft.client.renderer.entity.state.ZombieVillagerRenderState;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieVillagerRenderer.class)
public abstract class ZombieVillagerRendererMixin extends HumanoidMobRenderer<ZombieVillager, ZombieVillagerRenderState, ZombieVillagerModel<ZombieVillagerRenderState>> {
    public ZombieVillagerRendererMixin(EntityRendererProvider.Context context, ZombieVillagerModel<ZombieVillagerRenderState> humanoidModel, float f) {
        super(context, humanoidModel, f);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$addDecorationLayers(EntityRendererProvider.Context context, CallbackInfo ci) {
        addLayer(new GoatHornLayer<>(this, context));
    }
}
