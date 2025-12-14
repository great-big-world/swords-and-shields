package dev.creoii.greatbigworld.swordsandshields.mixin.client.render;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.object.book.BookModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.client.renderer.blockentity.state.EnchantTableRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantTableRenderer.class)
public class EnchantingTableBlockEntityRendererMixin {
    @Shadow @Final private BookModel bookModel;
    @Shadow @Final public static Material BOOK_TEXTURE;
    @Shadow @Final private MaterialSet materials;

    @Inject(method = "submit(Lnet/minecraft/client/renderer/blockentity/state/EnchantTableRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"))
    private void gbw$renderEnchantingTableBookGlint(EnchantTableRenderState enchantingTableBlockEntityRenderState, PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci, @Local BookModel.State bookModelState) {
        orderedRenderCommandQueue.submitModel(bookModel, bookModelState, matrixStack, RenderTypes.entityGlint(), enchantingTableBlockEntityRenderState.lightCoords, OverlayTexture.NO_OVERLAY, -1, materials.get(BOOK_TEXTURE), 0, enchantingTableBlockEntityRenderState.breakProgress);
    }
}
