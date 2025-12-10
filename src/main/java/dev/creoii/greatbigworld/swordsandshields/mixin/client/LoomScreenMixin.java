package dev.creoii.greatbigworld.swordsandshields.mixin.client;

import com.google.common.collect.Sets;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.client.GreatBigWorldClient;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BannerPatternTags;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(LoomScreen.class)
public class LoomScreenMixin {
    @WrapOperation(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;hasStack()Z", ordinal = 2))
    private boolean gbw$cancelDrawBannerPatternSlot(Slot instance, Operation<Integer> original) {
        return true;
    }

    @WrapOperation(method = "onInventoryChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;getStack()Lnet/minecraft/item/ItemStack;", ordinal = 3))
    private ItemStack gbw$cancelBannerPatternChanged(Slot instance, Operation<Integer> original) {
        return ItemStack.EMPTY;
    }

    @WrapOperation(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/LoomScreenHandler;getBannerPatterns()Ljava/util/List;"))
    private List<RegistryEntry<BannerPattern>> gbw$overrideBannerPatternsClient(LoomScreenHandler instance, Operation<List<RegistryEntry<BannerPattern>>> original) {
        ClientWorld world = MinecraftClient.getInstance().world;
        Registry<BannerPattern> registry = world.getRegistryManager().getOrThrow(RegistryKeys.BANNER_PATTERN);

        List<RegistryEntry<BannerPattern>> list = new ArrayList<>();

        registry.getOptional(BannerPatternTags.NO_ITEM_REQUIRED).ifPresent(named -> named.forEach(list::add));

        GreatBigWorldClient.getKnowledge().getOrDefault(Knowledge.Type.BANNER_PATTERN, Sets.newHashSet()).forEach(knowledge -> {
            if (registry.containsId(knowledge.data())) {
                list.add(registry.getEntry(registry.get(knowledge.data())));
            }
        });

        return list;
    }

    @WrapOperation(method = "getRows", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/LoomScreenHandler;getBannerPatterns()Ljava/util/List;"))
    private List<RegistryEntry<BannerPattern>> gbw$overrideBannerPatternsClient2(LoomScreenHandler instance, Operation<List<RegistryEntry<BannerPattern>>> original) {
        ClientWorld world = MinecraftClient.getInstance().world;
        Registry<BannerPattern> registry = world.getRegistryManager().getOrThrow(RegistryKeys.BANNER_PATTERN);

        List<RegistryEntry<BannerPattern>> list = new ArrayList<>();

        registry.getOptional(BannerPatternTags.NO_ITEM_REQUIRED).ifPresent(named -> named.forEach(list::add));

        GreatBigWorldClient.getKnowledge().getOrDefault(Knowledge.Type.BANNER_PATTERN, Sets.newHashSet()).forEach(knowledge -> {
            if (registry.containsId(knowledge.data())) {
                list.add(registry.getEntry(registry.get(knowledge.data())));
            }
        });

        return list;
    }

}
