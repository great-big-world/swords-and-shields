package dev.creoii.greatbigworld.swordsandshields.mixin.screen;

import dev.creoii.greatbigworld.element.Element;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import dev.creoii.greatbigworld.swordsandshields.util.ElementHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.IdMap;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantingTableBlock;

// TODO: Element power doesn't sway generated enchantments, just which one gets a level increase i think
@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentScreenHandlerMixin extends ScreenHandlerMixin {
    @Shadow @Final private RandomSource random;
    @Shadow public abstract int getGoldCount();
    @Shadow @Final public int[] levelClue;
    @Shadow @Final public int[] enchantClue;

    @Unique
    private final int[] elementPower = new int[Element.values().length];

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    private void gbw$initElementPower(int syncId, Inventory playerInventory, ContainerLevelAccess context, CallbackInfo ci) {
        Arrays.fill(elementPower, 0);
    }

    @Inject(method = "getEnchantmentList", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;get(Lnet/minecraft/tags/TagKey;)Ljava/util/Optional;"), cancellable = true)
    private void gbw$generateEnchantmentsFromKnowledge(RegistryAccess registryManager, ItemStack stack, int slot, int level, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        if (gbw$getPlayer() != null && !gbw$getPlayer().level().isClientSide()) {
            Registry<Enchantment> registry = registryManager.lookupOrThrow(Registries.ENCHANTMENT);
            KnowledgeManager knowledgeManager = KnowledgeManager.getServerState(gbw$getPlayer().level().getServer());
            List<Holder<Enchantment>> registryEntries = new ArrayList<>();
            Set<Knowledge> knowledges = knowledgeManager.getPlayerKnowledge(gbw$getPlayer(), Knowledge.Type.ENCHANTMENT);

            if (knowledges == null || knowledges.isEmpty()) {
                cir.setReturnValue(List.of());
                return;
            }

            knowledges.forEach(knowledge -> {
                Optional<Holder.Reference<Enchantment>> optional = registry.get(knowledge.data());
                optional.ifPresent(enchantmentReference -> {
                    if (enchantmentReference.is(EnchantmentTags.IN_ENCHANTING_TABLE))
                        registryEntries.add(enchantmentReference);
                });
            });

            if (registryEntries.isEmpty() || getGoldCount() <= 0) {
                cir.setReturnValue(List.of());
                return;
            }

            HolderSet<Enchantment> entries = HolderSet.direct(registryEntries);
            List<EnchantmentInstance> list = EnchantmentHelper.selectEnchantment(random, stack, level, entries.stream());
            if (stack.is(Items.BOOK) && list.size() > 1) {
                list.remove(random.nextInt(list.size()));
            }

            if (!list.isEmpty()) {
                List<EnchantmentInstance> weightedPool = new ArrayList<>();

                for (EnchantmentInstance entry : list) {
                    Element element = ElementHolder.gbw$getElement(entry.enchantment().unwrapKey().orElseThrow());
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
                    List<EnchantmentInstance> biased = new ArrayList<>();

                    int count = list.size();
                    RandomSource rand = random;

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

    @Inject(method = "method_17411", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;setSeed(J)V"))
    private void gbw$updateElementPower(ItemStack itemStack, Level world, BlockPos pos, CallbackInfo ci) {
        int[] add = new int[Element.values().length];
        for (BlockPos blockPos : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
            BlockPos up = pos.offset(blockPos.above());

            if (world.getBlockState(up).is(Blocks.COAL_BLOCK)) {
                ++add[Element.EARTH.ordinal()];
            } else if (world.getBlockState(up).is(Blocks.REDSTONE_BLOCK)) {
                ++add[Element.FIRE.ordinal()];
            } else if (world.getBlockState(up).is(Blocks.LAPIS_BLOCK)) {
                ++add[Element.WATER.ordinal()];
            } else if (world.getBlockState(up).is(Blocks.DIAMOND_BLOCK)) {
                ++add[Element.ICE.ordinal()];
            } else if (world.getBlockState(up).is(Blocks.WHITE_WOOL)) {
                ++add[Element.AIR.ordinal()];
            } else if (world.getBlockState(up).is(Blocks.GOLD_BLOCK)) {
                ++add[Element.LIGHTNING.ordinal()];
            } else if (world.getBlockState(up).is(Blocks.GLOWSTONE)) {
                ++add[Element.LIGHT.ordinal()];
            } else if (world.getBlockState(up).is(Blocks.TINTED_GLASS)) {
                ++add[Element.DARK.ordinal()];
            } else if (world.getBlockState(up).is(Blocks.BLACK_CONCRETE)) {
                ++add[Element.VOID.ordinal()];
            } else if (world.getBlockState(up).is(Blocks.GLASS)) {
                ++add[Element.AETHER.ordinal()];
            }
        }

        System.arraycopy(add, 0, elementPower, 0, elementPower.length);
    }

    @Inject(method = "method_17411", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/EnchantmentMenu;broadcastChanges()V"))
    private void gbw$modifyLevels(ItemStack itemStack, Level world, BlockPos pos, CallbackInfo ci) {
        IdMap<Holder<Enchantment>> indexedIterable = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).asHolderIdMap();

        for (int i = 0; i < getGoldCount() - 1; ++i) {
            int idx = weightedPick(random, enchantClue, levelClue, indexedIterable);
            if (idx < 0 || idx >= enchantClue.length || idx >= levelClue.length)
                continue;

            Holder<Enchantment> registryEntry = indexedIterable.byId(enchantClue[idx]);
            if (registryEntry == null)
                continue;

            levelClue[idx] = Math.min(levelClue[idx] + 1, registryEntry.value().getMaxLevel());
        }
    }

    @Unique
    private int weightedPick(RandomSource random, int[] ids, int[] levels, IdMap<Holder<Enchantment>> indexedIterable) {
        int total = 0;

        for (int i = 0; i < ids.length; ++i) {
            if (ids[i] < 0)
                continue;

            Holder<Enchantment> registryEntry = indexedIterable.byId(ids[i]);
            if (registryEntry == null)
                continue;

            Enchantment enchantment = registryEntry.value();

            int add = enchantment.getWeight() * (enchantment.getMaxLevel() - levels[i] + 1);

            Element element = ElementHolder.gbw$getElement(registryEntry.unwrapKey().orElseThrow());
            double mod = 1d / (Math.log(elementPower[element.ordinal()] + 2) / Math.log(2));
            total += (int) Math.max(1, add * mod);
        }

        if (total <= 0)
            return ids.length - 1;

        int roll = random.nextInt(total);

        for (int i = 0; i < ids.length; i++) {
            if (ids[i] < 0)
                continue;

            Holder<Enchantment> registryEntry = indexedIterable.byId(ids[i]);
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
