package dev.creoii.greatbigworld.swordsandshields.mixin.client;

import net.minecraft.block.entity.EnchantingTableBlockEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.EnchantingTableBlockEntityRenderer;
import net.minecraft.client.render.entity.model.BookModel;
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

    @Inject(method = "render(Lnet/minecraft/block/entity/EnchantingTableBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
    private void gbw$renderEnchantingTableBookGlint(EnchantingTableBlockEntity enchantingTableBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci) {
        book.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEntityGlint()), i, OverlayTexture.DEFAULT_UV);
    }
}
