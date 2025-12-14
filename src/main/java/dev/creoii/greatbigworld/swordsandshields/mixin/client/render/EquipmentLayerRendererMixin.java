package dev.creoii.greatbigworld.swordsandshields.mixin.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.creoii.greatbigworld.swordsandshields.util.SwordsAndShieldsTags;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EquipmentLayerRenderer.class)
public class EquipmentLayerRendererMixin {
    @Inject(method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V", at = @At(value = "INVOKE", target = "Ljava/util/function/Function;apply(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 1), cancellable = true)
    private <S> void gbw$injectDecorationTrims(EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> resourceKey, Model<? super S> model, S object, ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, @Nullable Identifier identifier, int j, int k, CallbackInfo ci) {
        ArmorTrim armorTrim = itemStack.get(DataComponents.TRIM);
        if (armorTrim != null && armorTrim.material().is(SwordsAndShieldsTags.DECORATION)) {
            ci.cancel();
        }
    }
}
