package dev.creoii.greatbigworld.swordsandshields.mixin.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BannerPatternTags;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.slot.Slot;
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

@Mixin(LoomScreenHandler.class)
public class LoomScreenHandlerMixin extends ScreenHandlerMixin {
    @Shadow
    @Final
    private RegistryEntryLookup<BannerPattern> bannerPatternLookup;

    @WrapOperation(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/LoomScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 2))
    private Slot gbw$removeBannerPatternSlot(LoomScreenHandler instance, Slot slot, Operation<Slot> original) {
        return null;
    }

    @WrapOperation(method = "onContentChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;getStack()Lnet/minecraft/item/ItemStack;", ordinal = 2))
    private ItemStack gbw$removeBannerPatternSlot(Slot instance, Operation<ItemStack> original) {
        return ItemStack.EMPTY;
    }

    @WrapOperation(method = "quickMove", at = @At(value = "FIELD", target = "Lnet/minecraft/screen/slot/Slot;id:I", ordinal = 3, opcode = Opcodes.GETFIELD))
    private int gbw$cancelBannerPatternQuickMove1(Slot instance, Operation<Integer> original) {
        return 0;
    }

    @WrapOperation(method = "quickMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;contains(Lnet/minecraft/component/ComponentType;)Z"))
    private boolean gbw$cancelBannerPatternQuickMove2(ItemStack instance, ComponentType<?> componentType, Operation<Boolean> original) {
        return false;
    }

    @Inject(method = "getPatternsFor", at = @At("HEAD"), cancellable = true)
    private void gbw$allowLearnedBannerPatterns(ItemStack stack, CallbackInfoReturnable<List<RegistryEntry<BannerPattern>>> cir) {
        List<RegistryEntry<BannerPattern>> list = new ArrayList<>();
        bannerPatternLookup.getOptional(BannerPatternTags.NO_ITEM_REQUIRED).ifPresent(named -> named.forEach(list::add));

        if (gbw$getPlayer() != null) {
            Registry<BannerPattern> registry = gbw$getPlayer().getEntityWorld().getRegistryManager().getOrThrow(RegistryKeys.BANNER_PATTERN);
            if (!gbw$getPlayer().getEntityWorld().isClient()) {
                KnowledgeManager knowledgeManager = KnowledgeManager.getServerState(gbw$getPlayer().getEntityWorld().getServer());
                Set<Knowledge> knowledges = knowledgeManager.getPlayerKnowledge(gbw$getPlayer(), Knowledge.Type.BANNER_PATTERN);

                if (knowledges != null) {
                    knowledges.forEach(knowledge -> {
                        if (registry.containsId(knowledge.data())) {
                            list.add(registry.getEntry(registry.get(knowledge.data())));
                        }
                    });
                }
            }
        }

        cir.setReturnValue(list);
    }
}
