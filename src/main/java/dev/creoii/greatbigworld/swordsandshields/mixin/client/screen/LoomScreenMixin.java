package dev.creoii.greatbigworld.swordsandshields.mixin.client.screen;

import com.google.common.collect.Sets;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.client.GreatBigWorldClient;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BannerPatternTags;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;

@Mixin(LoomScreen.class)
public class LoomScreenMixin {
    @WrapOperation(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;hasItem()Z", ordinal = 2))
    private boolean gbw$cancelDrawBannerPatternSlot(Slot instance, Operation<Integer> original) {
        return true;
    }

    @WrapOperation(method = "containerChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;getItem()Lnet/minecraft/world/item/ItemStack;", ordinal = 3))
    private ItemStack gbw$cancelBannerPatternChanged(Slot instance, Operation<Integer> original) {
        return ItemStack.EMPTY;
    }

    @WrapOperation(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/LoomMenu;getSelectablePatterns()Ljava/util/List;"))
    private List<Holder<BannerPattern>> gbw$overrideBannerPatternsClient(LoomMenu instance, Operation<List<Holder<BannerPattern>>> original) {
        ClientLevel world = Minecraft.getInstance().level;
        Registry<BannerPattern> registry = world.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN);

        List<Holder<BannerPattern>> list = new ArrayList<>();

        registry.get(BannerPatternTags.NO_ITEM_REQUIRED).ifPresent(named -> named.forEach(list::add));

        GreatBigWorldClient.getKnowledge().getOrDefault(Knowledge.Type.BANNER_PATTERN, Sets.newHashSet()).forEach(knowledge -> {
            if (registry.containsKey(knowledge.data())) {
                list.add(registry.wrapAsHolder(registry.getValue(knowledge.data())));
            }
        });

        return list;
    }

    @WrapOperation(method = "totalRowCount", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/LoomMenu;getSelectablePatterns()Ljava/util/List;"))
    private List<Holder<BannerPattern>> gbw$overrideBannerPatternsClient2(LoomMenu instance, Operation<List<Holder<BannerPattern>>> original) {
        ClientLevel world = Minecraft.getInstance().level;
        Registry<BannerPattern> registry = world.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN);

        List<Holder<BannerPattern>> list = new ArrayList<>();

        registry.get(BannerPatternTags.NO_ITEM_REQUIRED).ifPresent(named -> named.forEach(list::add));

        GreatBigWorldClient.getKnowledge().getOrDefault(Knowledge.Type.BANNER_PATTERN, Sets.newHashSet()).forEach(knowledge -> {
            if (registry.containsKey(knowledge.data())) {
                list.add(registry.wrapAsHolder(registry.getValue(knowledge.data())));
            }
        });

        return list;
    }

}
