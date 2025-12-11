package dev.creoii.greatbigworld.swordsandshields.mixin.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BannerPatternTags;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;

@Mixin(LoomMenu.class)
public class LoomScreenHandlerMixin extends ScreenHandlerMixin {
    @Shadow
    @Final
    private HolderGetter<BannerPattern> patternGetter;

    @WrapOperation(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/LoomMenu;addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;", ordinal = 2))
    private Slot gbw$removeBannerPatternSlot(LoomMenu instance, Slot slot, Operation<Slot> original) {
        return null;
    }

    @WrapOperation(method = "slotsChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;getItem()Lnet/minecraft/world/item/ItemStack;", ordinal = 2))
    private ItemStack gbw$removeBannerPatternSlot(Slot instance, Operation<ItemStack> original) {
        return ItemStack.EMPTY;
    }

    @WrapOperation(method = "quickMoveStack", at = @At(value = "FIELD", target = "Lnet/minecraft/world/inventory/Slot;index:I", ordinal = 3, opcode = Opcodes.GETFIELD))
    private int gbw$cancelBannerPatternQuickMove1(Slot instance, Operation<Integer> original) {
        return 0;
    }

    @WrapOperation(method = "quickMoveStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"))
    private boolean gbw$cancelBannerPatternQuickMove2(ItemStack instance, DataComponentType<?> componentType, Operation<Boolean> original) {
        return false;
    }

    @Inject(method = "getSelectablePatterns(Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private void gbw$allowLearnedBannerPatterns(ItemStack stack, CallbackInfoReturnable<List<Holder<BannerPattern>>> cir) {
        List<Holder<BannerPattern>> list = new ArrayList<>();
        patternGetter.get(BannerPatternTags.NO_ITEM_REQUIRED).ifPresent(named -> named.forEach(list::add));

        if (gbw$getPlayer() != null) {
            Registry<BannerPattern> registry = gbw$getPlayer().level().registryAccess().lookupOrThrow(Registries.BANNER_PATTERN);
            if (!gbw$getPlayer().level().isClientSide()) {
                KnowledgeManager knowledgeManager = KnowledgeManager.getServerState(gbw$getPlayer().level().getServer());
                Set<Knowledge> knowledges = knowledgeManager.getPlayerKnowledge(gbw$getPlayer(), Knowledge.Type.BANNER_PATTERN);

                if (knowledges != null) {
                    knowledges.forEach(knowledge -> {
                        if (registry.containsKey(knowledge.data())) {
                            list.add(registry.wrapAsHolder(registry.getValue(knowledge.data())));
                        }
                    });
                }
            }
        }

        cir.setReturnValue(list);
    }
}
