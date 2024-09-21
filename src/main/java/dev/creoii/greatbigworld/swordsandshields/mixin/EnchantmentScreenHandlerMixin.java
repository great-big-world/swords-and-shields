package dev.creoii.greatbigworld.swordsandshields.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentPlayer;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.BiConsumer;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin extends ScreenHandler {
    @Shadow @Final public int[] enchantmentPower;
    @Shadow @Final private Inventory inventory;
    @Shadow @Final private Property seed;
    @Shadow @Final private Random random;

    protected EnchantmentScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Redirect(method = "onButtonClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandlerContext;run(Ljava/util/function/BiConsumer;)V"))
    private void gbw$generateEnchantmentsFromKnown(ScreenHandlerContext instance, BiConsumer<World, BlockPos> function, @Local(argsOnly = true) PlayerEntity player, @Local(argsOnly = true) int id, @Local(ordinal = 0) ItemStack itemStack, @Local(ordinal = 1) ItemStack itemStack2) {
        instance.run((world, blockPos) -> runButtonClick(player, id, itemStack, itemStack2, world, blockPos));
    }

    @Unique
    private void runButtonClick(PlayerEntity player, int id, ItemStack itemStack, ItemStack itemStack2, World world, BlockPos pos) {
        ItemStack itemStack3 = itemStack;
        List<EnchantmentLevelEntry> list = generateValidEnchantments(player, world.getEnabledFeatures(), itemStack3, id, enchantmentPower[id]);
        int i = id + 1;
        //list.forEach(entry -> System.out.println(entry.enchantment.getTranslationKey()));
        if (!list.isEmpty()) {
            player.applyEnchantmentCosts(itemStack3, i);
            if (itemStack3.isOf(Items.BOOK)) {
                itemStack3 = itemStack.copyComponentsToNewStack(Items.ENCHANTED_BOOK, 1);
                inventory.setStack(0, itemStack3);
            }

            for (EnchantmentLevelEntry enchantmentLevelEntry : list) {
                itemStack3.addEnchantment(enchantmentLevelEntry.enchantment, enchantmentLevelEntry.level);
            }

            if (!player.isInCreativeMode()) {
                itemStack2.decrement(i);
                if (itemStack2.isEmpty()) {
                    inventory.setStack(1, ItemStack.EMPTY);
                }
            }

            player.incrementStat(Stats.ENCHANT_ITEM);
            if (player instanceof ServerPlayerEntity) {
                Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, itemStack3, i);
            }

            inventory.markDirty();
            seed.set(player.getEnchantmentTableSeed());
            onContentChanged(inventory);
            world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1f, world.random.nextFloat() * .1f + .9f);
        }
    }

    @Unique
    private List<EnchantmentLevelEntry> generateValidEnchantments(PlayerEntity player, FeatureSet enabledFeatures, ItemStack stack, int slot, int level) {
        random.setSeed(seed.get() + slot);
        List<EnchantmentLevelEntry> list = EnchantmentHelper.generateEnchantments(enabledFeatures, random, stack, level, false);
        if (stack.isOf(Items.BOOK) && list.size() > 1) {
            list.remove(random.nextInt(list.size()));
        }

        return list.stream().filter(entry -> {
            return player instanceof EnchantmentPlayer enchantmentPlayer && enchantmentPlayer.gbw$getEnchantments().contains(entry.enchantment);
        }).toList();
    }
}
