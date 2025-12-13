package dev.creoii.greatbigworld.swordsandshields.mixin.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    @WrapOperation(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;hasInfiniteMaterials()Z"))
    private boolean gbw$dontTakeLevels(Player instance, Operation<Boolean> original) {
        return true;
    }

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    private void gbw$allowPickupAlways(Player player, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @ModifyConstant(method = "createInputSlotDefinitions", constant = @Constant(intValue = 47, ordinal = 0))
    private static int gbw$modifySlotY1(int constant) {
        return 46;
    }

    @ModifyConstant(method = "createInputSlotDefinitions", constant = @Constant(intValue = 47, ordinal = 1))
    private static int gbw$modifySlotY2(int constant) {
        return 46;
    }

    @ModifyConstant(method = "createInputSlotDefinitions", constant = @Constant(intValue = 47, ordinal = 2))
    private static int gbw$modifySlotY3(int constant) {
        return 83;
    }

    @ModifyConstant(method = "createInputSlotDefinitions", constant = @Constant(intValue = 27))
    private static int gbw$modifySlotX1(int constant) {
        return 22;
    }

    @ModifyConstant(method = "createInputSlotDefinitions", constant = @Constant(intValue = 76))
    private static int gbw$modifySlotX2(int constant) {
        return 49;
    }

    @ModifyConstant(method = "createInputSlotDefinitions", constant = @Constant(intValue = 134))
    private static int gbw$modifySlotX3(int constant) {
        return 36;
    }
}
