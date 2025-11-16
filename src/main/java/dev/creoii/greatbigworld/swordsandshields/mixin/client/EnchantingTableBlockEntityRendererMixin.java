package dev.creoii.greatbigworld.swordsandshields.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.EnchantingTableBlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.EnchantingTableBlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantingTableBlockEntityRenderer.class)
public class EnchantingTableBlockEntityRendererMixin {
    @Shadow @Final private BookModel book;
    @Shadow @Final public static SpriteIdentifier BOOK_TEXTURE;
    @Shadow @Final private SpriteHolder spriteHolder;

    @Inject(method = "render(Lnet/minecraft/client/render/block/entity/state/EnchantingTableBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
    private void gbw$renderEnchantingTableBookGlint(EnchantingTableBlockEntityRenderState enchantingTableBlockEntityRenderState, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci, @Local BookModel.BookModelState bookModelState) {
        orderedRenderCommandQueue.submitModel(book, bookModelState, matrixStack, RenderLayer.getEntityGlint(), enchantingTableBlockEntityRenderState.lightmapCoordinates, OverlayTexture.DEFAULT_UV, -1, spriteHolder.getSprite(BOOK_TEXTURE), 0, enchantingTableBlockEntityRenderState.crumblingOverlay);
    }
}
