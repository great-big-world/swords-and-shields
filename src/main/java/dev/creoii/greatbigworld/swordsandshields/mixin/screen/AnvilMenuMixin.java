package dev.creoii.greatbigworld.swordsandshields.mixin.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    @Shadow
    private boolean onlyRenaming;

    public AnvilMenuMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, ItemCombinerMenuSlotDefinition itemCombinerMenuSlotDefinition) {
        super(menuType, i, inventory, containerLevelAccess, itemCombinerMenuSlotDefinition);
    }

    @WrapOperation(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;hasInfiniteMaterials()Z"))
    private boolean gbw$dontTakeLevels(Player instance, Operation<Boolean> original) {
        return true;
    }

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    private void gbw$allowPickupAlways(Player player, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @WrapOperation(method = "setItemName", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;"))
    private MutableComponent gbw$colorItemName(String string, Operation<MutableComponent> original) {
        ItemStack stack = inputSlots.getItem(1);
        if (stack.getItem() instanceof DyeItem dyeItem) {
            return original.call(string).withColor(dyeItem.getDyeColor().getTextColor());
        }
        return original.call(string);
    }

    @WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean gbw$allowDyeMatch(ItemStack instance, Item item, Operation<Boolean> original, @Local(ordinal = 2) ItemStack itemStack3) {
        return original.call(instance, item) || itemStack3.getItem() instanceof DyeItem;
    }

    @WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z", ordinal = 1))
    private boolean gbw$allowDyeNonRenamedItems(ItemStack instance, DataComponentType dataComponentType, Operation<Boolean> original, @Local(ordinal = 2) ItemStack itemStack3) {
        return original.call(instance, dataComponentType) || itemStack3.getItem() instanceof DyeItem;
    }

    @WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 4))
    private void gbw$colorItemName(ResultContainer instance, int i, ItemStack itemStack, Operation<Void> original) {
        ItemStack itemStack3 = inputSlots.getItem(1);
        if (itemStack3.getItem() instanceof DyeItem dyeItem) {
            Component oldName = itemStack.get(DataComponents.CUSTOM_NAME);
            if (oldName == null) {
                oldName = itemStack.getItemName().copy();
            }
            itemStack.set(DataComponents.CUSTOM_NAME, oldName.copy().withColor(dyeItem.getDyeColor().getTextColor()));
            original.call(instance, i, itemStack);
            onlyRenaming = false;
            return;
        }
        original.call(instance, i, itemStack);
    }

    @WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z"))
    private boolean gbw$allowDyeAlways(String instance, Object o, Operation<Boolean> original, @Local(ordinal = 2) ItemStack itemStack3) {
        return original.call(instance, o) && !(itemStack3.getItem() instanceof DyeItem);
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
