package dev.creoii.greatbigworld.swordsandshields.mixin.screen;

import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

// Elemental cost applied to enchantments
// In-world interaction will sway the table towards picking more of that element
@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin extends ScreenHandlerMixin {
    @Shadow @Final private Random random;
    @Shadow public abstract int getLapisCount();

    @Shadow
    @Final
    public int[] enchantmentLevel;

    @Shadow
    @Final
    public int[] enchantmentId;

    @Inject(method = "generateEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Registry;getOptional(Lnet/minecraft/registry/tag/TagKey;)Ljava/util/Optional;"), cancellable = true)
    private void gbw$generateEnchantmentsFromKnowledge(DynamicRegistryManager registryManager, ItemStack stack, int slot, int level, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        if (gbw$getPlayer() != null && !gbw$getPlayer().getEntityWorld().isClient()) {
            Registry<Enchantment> registry = registryManager.getOrThrow(RegistryKeys.ENCHANTMENT);
            KnowledgeManager knowledgeManager = KnowledgeManager.getServerState(gbw$getPlayer().getEntityWorld().getServer());
            List<RegistryEntry<Enchantment>> registryEntries = new ArrayList<>();
            Set<Knowledge> knowledges = knowledgeManager.getPlayerKnowledge(gbw$getPlayer(), Knowledge.Type.ENCHANTMENT);

            if (knowledges == null || knowledges.isEmpty()) {
                cir.setReturnValue(List.of());
                return;
            }

            knowledges.forEach(knowledge -> {
                Optional<RegistryEntry.Reference<Enchantment>> optional = registry.getEntry(knowledge.data());
                optional.ifPresent(enchantmentReference -> {
                    if (enchantmentReference.isIn(EnchantmentTags.IN_ENCHANTING_TABLE))
                        registryEntries.add(enchantmentReference);
                });
            });

            if (registryEntries.isEmpty() || getLapisCount() <= 0) {
                cir.setReturnValue(List.of());
                return;
            }

            RegistryEntryList<Enchantment> entries = RegistryEntryList.of(registryEntries);
            List<EnchantmentLevelEntry> list = EnchantmentHelper.generateEnchantments(random, stack, level, entries.stream());
            if (stack.isOf(Items.BOOK) && list.size() > 1) {
                list.remove(random.nextInt(list.size()));
            }

            cir.setReturnValue(list);
            return;
        }
        cir.setReturnValue(List.of());
    }

    @Inject(method = "method_17411", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/EnchantmentScreenHandler;sendContentUpdates()V"))
    private void gbw$modifyLevels(ItemStack itemStack, World world, BlockPos pos, CallbackInfo ci) {
        IndexedIterable<RegistryEntry<Enchantment>> indexedIterable = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getIndexedEntries();

        for (int i = 0; i < getLapisCount() - 1; ++i) {
            int idx = weightedPick(random, enchantmentId, enchantmentLevel, indexedIterable);
            if (idx < 0)
                continue;
            enchantmentLevel[idx] = Math.min(enchantmentLevel[idx] + 1, indexedIterable.get(enchantmentId[idx]).value().getMaxLevel());
        }
    }

    @Unique
    private int weightedPick(Random random, int[] ids, int[] levels, IndexedIterable<RegistryEntry<Enchantment>> indexedIterable) {
        int total = 0;

        for (int i = 0; i < ids.length; ++i) {
            if (ids[i] < 0)
                continue;

            Enchantment enchantment = indexedIterable.get(ids[i]).value();
            total += enchantment.getWeight() * (enchantment.getMaxLevel() - levels[i] + 1);
        }

        if (total <= 0)
            return ids.length - 1;

        int roll = random.nextInt(total);

        for (int i = 0; i < ids.length; i++) {
            if (ids[i] < 0)
                continue;

            Enchantment enchantment = indexedIterable.get(ids[i]).value();
            roll -= enchantment.getWeight() * (enchantment.getMaxLevel() - levels[i] + 1);
            if (roll < 0)
                return i;
        }

        return ids.length - 1;
    }
}
