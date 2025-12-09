package dev.creoii.greatbigworld.swordsandshields.mixin.screen;

import dev.creoii.greatbigworld.element.Element;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import dev.creoii.greatbigworld.swordsandshields.util.ElementHolder;
import net.minecraft.block.Blocks;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
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

//TODO: Always applies level 1 or 2 to item
// TODO: Element power doesn't sway generated enchantments, just which one gets a level increase i think
@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin extends ScreenHandlerMixin {
    @Shadow @Final private Random random;
    @Shadow public abstract int getLapisCount();
    @Shadow @Final public int[] enchantmentLevel;
    @Shadow @Final public int[] enchantmentId;

    @Unique
    private final int[] elementPower = new int[Element.values().length];

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At("TAIL"))
    private void gbw$initElementPower(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CallbackInfo ci) {
        Arrays.fill(elementPower, 0);
    }

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

            if (!list.isEmpty()) {
                List<EnchantmentLevelEntry> weightedPool = new ArrayList<>();

                for (EnchantmentLevelEntry entry : list) {
                    Element element = ElementHolder.gbw$getElement(entry.enchantment().getKey().orElseThrow());
                    if (element == null) {
                        weightedPool.add(entry);
                        continue;
                    }

                    int mod = elementPower[element.ordinal()];

                    double boost = 1d + (Math.log(mod + 2) / Math.log(2));

                    int weight = Math.max(1, (int) (entry.enchantment().value().getWeight() * boost));

                    for (int i = 0; i < weight; ++i) {
                        weightedPool.add(entry);
                    }
                }

                if (!weightedPool.isEmpty()) {
                    List<EnchantmentLevelEntry> biased = new ArrayList<>();

                    int count = list.size();
                    Random rand = random;

                    for (int i = 0; i < count; ++i) {
                        biased.add(weightedPool.get(rand.nextInt(weightedPool.size())));
                    }

                    list.clear();
                    list.addAll(biased);
                }
            }

            cir.setReturnValue(list);
            return;
        }
        cir.setReturnValue(List.of());
    }

    @Inject(method = "method_17411", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;setSeed(J)V"))
    private void gbw$updateElementPower(ItemStack itemStack, World world, BlockPos pos, CallbackInfo ci) {
        int[] add = new int[Element.values().length];
        for (BlockPos blockPos : EnchantingTableBlock.POWER_PROVIDER_OFFSETS) {
            BlockPos up = pos.add(blockPos.up());

            if (world.getBlockState(up).isOf(Blocks.COAL_BLOCK)) {
                ++add[Element.EARTH.ordinal()];
            } else if (world.getBlockState(up).isOf(Blocks.REDSTONE_BLOCK)) {
                ++add[Element.FIRE.ordinal()];
            } else if (world.getBlockState(up).isOf(Blocks.LAPIS_BLOCK)) {
                ++add[Element.WATER.ordinal()];
            } else if (world.getBlockState(up).isOf(Blocks.DIAMOND_BLOCK)) {
                ++add[Element.ICE.ordinal()];
            } else if (world.getBlockState(up).isOf(Blocks.WHITE_WOOL)) {
                ++add[Element.AIR.ordinal()];
            } else if (world.getBlockState(up).isOf(Blocks.GOLD_BLOCK)) {
                ++add[Element.LIGHTNING.ordinal()];
            } else if (world.getBlockState(up).isOf(Blocks.GLOWSTONE)) {
                ++add[Element.LIGHT.ordinal()];
            } else if (world.getBlockState(up).isOf(Blocks.TINTED_GLASS)) {
                ++add[Element.DARK.ordinal()];
            } else if (world.getBlockState(up).isOf(Blocks.BLACK_CONCRETE)) {
                ++add[Element.VOID.ordinal()];
            } else if (world.getBlockState(up).isOf(Blocks.GLASS)) {
                ++add[Element.AETHER.ordinal()];
            }
        }

        System.arraycopy(add, 0, elementPower, 0, elementPower.length);
    }

    @Inject(method = "method_17411", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/EnchantmentScreenHandler;sendContentUpdates()V"))
    private void gbw$modifyLevels(ItemStack itemStack, World world, BlockPos pos, CallbackInfo ci) {
        IndexedIterable<RegistryEntry<Enchantment>> indexedIterable = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getIndexedEntries();

        for (int i = 0; i < getLapisCount() - 1; ++i) {
            int idx = weightedPick(random, enchantmentId, enchantmentLevel, indexedIterable);
            if (idx < 0 || idx >= enchantmentId.length || idx >= enchantmentLevel.length)
                continue;

            RegistryEntry<Enchantment> registryEntry = indexedIterable.get(enchantmentId[idx]);
            if (registryEntry == null)
                continue;

            enchantmentLevel[idx] = Math.min(enchantmentLevel[idx] + 1, registryEntry.value().getMaxLevel());
        }
    }

    @Unique
    private int weightedPick(Random random, int[] ids, int[] levels, IndexedIterable<RegistryEntry<Enchantment>> indexedIterable) {
        int total = 0;

        for (int i = 0; i < ids.length; ++i) {
            if (ids[i] < 0)
                continue;

            RegistryEntry<Enchantment> registryEntry = indexedIterable.get(ids[i]);
            if (registryEntry == null)
                continue;

            Enchantment enchantment = registryEntry.value();

            int add = enchantment.getWeight() * (enchantment.getMaxLevel() - levels[i] + 1);

            Element element = ElementHolder.gbw$getElement(registryEntry.getKey().orElseThrow());
            double mod = 1d / (Math.log(elementPower[element.ordinal()] + 2) / Math.log(2));
            total += (int) Math.max(1, add * mod);
        }

        if (total <= 0)
            return ids.length - 1;

        int roll = random.nextInt(total);

        for (int i = 0; i < ids.length; i++) {
            if (ids[i] < 0)
                continue;

            RegistryEntry<Enchantment> registryEntry = indexedIterable.get(ids[i]);
            if (registryEntry == null)
                continue;

            Enchantment enchantment = registryEntry.value();
            roll -= enchantment.getWeight() * (enchantment.getMaxLevel() - levels[i] + 1);
            if (roll < 0)
                return i;
        }

        return ids.length - 1;
    }
}
