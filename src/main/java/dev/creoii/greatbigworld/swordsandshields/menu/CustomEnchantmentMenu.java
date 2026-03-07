package dev.creoii.greatbigworld.swordsandshields.menu;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsScreens;
import dev.creoii.greatbigworld.swordsandshields.util.ExtendedScreenHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantingTableBlock;

public class CustomEnchantmentMenu extends AbstractContainerMenu {
    static final Identifier EMPTY_SLOT_LAPIS_LAZULI = Identifier.withDefaultNamespace("container/slot/lapis_lazuli");
    private final Container enchantSlots = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            super.setChanged();
            CustomEnchantmentMenu.this.slotsChanged(this);
        }
    };
    private final ContainerLevelAccess access;
    private final RandomSource random = RandomSource.create();
    private final DataSlot enchantmentSeed = DataSlot.standalone();
    public final int[] costs = new int[3];
    public final int[] enchantClue = new int[]{-1, -1, -1};
    public final int[] levelClue = new int[]{-1, -1, -1};

    public CustomEnchantmentMenu(int i, Inventory inventory) {
        this(i, inventory, ContainerLevelAccess.NULL);
    }

    public CustomEnchantmentMenu(int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(SwordsAndShieldsScreens.ENCHANTING, i);
        access = containerLevelAccess;
        addSlot(new Slot(enchantSlots, 0, 15, 47){
            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        addSlot(new Slot(enchantSlots, 1, 35, 47){
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return itemStack.is(Items.LAPIS_LAZULI);
            }

            @Override
            public Identifier getNoItemIcon() {
                return EMPTY_SLOT_LAPIS_LAZULI;
            }
        });
        addStandardInventorySlots(inventory, 8, 84);
        addDataSlot(DataSlot.shared(costs, 0));
        addDataSlot(DataSlot.shared(costs, 1));
        addDataSlot(DataSlot.shared(costs, 2));
        addDataSlot(enchantmentSeed).set(inventory.player.getEnchantmentSeed());
        addDataSlot(DataSlot.shared(enchantClue, 0));
        addDataSlot(DataSlot.shared(enchantClue, 1));
        addDataSlot(DataSlot.shared(enchantClue, 2));
        addDataSlot(DataSlot.shared(levelClue, 0));
        addDataSlot(DataSlot.shared(levelClue, 1));
        addDataSlot(DataSlot.shared(levelClue, 2));
    }

    @Override
    public void slotsChanged(Container container) {
        if (container == enchantSlots) {
            ItemStack itemStack = container.getItem(0);
            if (itemStack.isEmpty() || !itemStack.isEnchantable()) {
                for (int i = 0; i < 3; ++i) {
                    costs[i] = 0;
                    enchantClue[i] = -1;
                    levelClue[i] = -1;
                }
            } else {
                access.execute((level, blockPos) -> {
                    int j;
                    IdMap<Holder<Enchantment>> idMap = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).asHolderIdMap();
                    int i = 0;
                    for (BlockPos blockPos2 : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
                        if (!EnchantingTableBlock.isValidBookShelf(level, blockPos, blockPos2))
                            continue;
                        ++i;
                    }

                    random.setSeed(enchantmentSeed.get());
                    for (j = 0; j < 3; ++j) {
                        costs[j] = EnchantmentHelper.getEnchantmentCost(random, j, i, itemStack);
                        enchantClue[j] = -1;
                        levelClue[j] = -1;
                        if (costs[j] >= j + 1)
                            continue;
                        costs[j] = 0;
                    }

                    for (j = 0; j < 3; ++j) {
                        List<EnchantmentInstance> list = getKnowledgeEnchantmentList(level, ((ExtendedScreenHandler) this).gbw$getPlayer(), level.registryAccess(), itemStack, j, costs[j]);
                        if (costs[j] <= 0 || list.isEmpty())
                            continue;

                        EnchantmentInstance instance = list.get(random.nextInt(list.size()));
                        if (instance.level() > 0) {
                            enchantClue[j] = idMap.getId(instance.enchantment());
                            levelClue[j] = instance.level();
                        }
                    }

                    for (int a = 0; a < 2; a++) {
                        for (int b = a + 1; b < 3; b++) {
                            if (costs[a] == 0 && costs[b] > 0) {
                                int c = costs[a];
                                costs[a] = costs[b];
                                costs[b] = c;

                                c = enchantClue[a];
                                enchantClue[a] = enchantClue[b];
                                enchantClue[b] = c;

                                c = levelClue[a];
                                levelClue[a] = levelClue[b];
                                levelClue[b] = c;
                            }
                        }
                    }

                    broadcastChanges();
                });
            }
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int i) {
        if (i < 0 || i >= costs.length) {
            Util.logAndPauseIfInIde(player.getPlainTextName() + " pressed invalid button id: " + i);
            return false;
        }
        ItemStack itemStack = enchantSlots.getItem(0);
        ItemStack itemStack2 = enchantSlots.getItem(1);
        int j = i + 1;
        if ((itemStack2.isEmpty() || itemStack2.getCount() < j) && !player.hasInfiniteMaterials()) {
            return false;
        }

        if (costs[i] > 0 && !itemStack.isEmpty() && (player.experienceLevel >= j && player.experienceLevel >= costs[i] || player.hasInfiniteMaterials())) {
            access.execute((level, blockPos) -> {
                ItemStack itemStack3 = itemStack;
                List<EnchantmentInstance> list = getKnowledgeEnchantmentList(level, ((ExtendedScreenHandler) this).gbw$getPlayer(), level.registryAccess(), itemStack3, i, costs[i]);
                if (!list.isEmpty()) {
                    player.onEnchantmentPerformed(itemStack3, j);
                    if (itemStack3.is(Items.BOOK)) {
                        itemStack3 = itemStack.transmuteCopy(Items.ENCHANTED_BOOK);
                        enchantSlots.setItem(0, itemStack3);
                    }

                    for (EnchantmentInstance enchantmentInstance : list) {
                        itemStack3.enchant(enchantmentInstance.enchantment(), enchantmentInstance.level());
                    }

                    itemStack2.consume(j, player);
                    if (itemStack2.isEmpty()) {
                        enchantSlots.setItem(1, ItemStack.EMPTY);
                    }

                    player.awardStat(Stats.ENCHANT_ITEM);
                    if (player instanceof ServerPlayer) {
                        CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayer)player, itemStack3, j);
                    }

                    enchantSlots.setChanged();
                    enchantmentSeed.set(player.getEnchantmentSeed());
                    slotsChanged(enchantSlots);
                    level.playSound(null, blockPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0f, level.random.nextFloat() * 0.1f + 0.9f);
                }
            });
            return true;
        }
        return false;
    }

    public int getGoldCount() {
        ItemStack itemStack = enchantSlots.getItem(1);
        if (itemStack.isEmpty()) {
            return 0;
        }
        return itemStack.getCount();
    }

    public int getEnchantmentSeed() {
        return enchantmentSeed.get();
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        access.execute((level, blockPos) -> clearContainer(player, enchantSlots));
    }

    @Override
    public boolean stillValid(Player player) {
        return CustomEnchantmentMenu.stillValid(access, player, Blocks.ENCHANTING_TABLE);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(i);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (i == 0) {
                if (!moveItemStackTo(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (i == 1) {
                if (!moveItemStackTo(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemStack2.is(Items.LAPIS_LAZULI)) {
                if (!moveItemStackTo(itemStack2, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!slots.getFirst().hasItem() && slots.getFirst().mayPlace(itemStack2)) {
                ItemStack itemStack3 = itemStack2.copyWithCount(1);
                itemStack2.shrink(1);
                slots.getFirst().setByPlayer(itemStack3);
            } else {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack2);
        }
        return itemStack;
    }

    public List<EnchantmentInstance> getKnowledgeEnchantmentList(Level level, Player player, RegistryAccess registryAccess, ItemStack stack, int i, int j) {
        random.setSeed(enchantmentSeed.get() + i);

        Optional<HolderSet.Named<Enchantment>> optional = registryAccess.lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.IN_ENCHANTING_TABLE);
        if (optional.isEmpty())
            return List.of();

        KnowledgeManager manager = KnowledgeManager.getServerState(level.getServer());
        Set<Knowledge> knowledges = manager.getPlayerKnowledge(player, Knowledge.Type.ENCHANTMENT);

        if (knowledges == null || knowledges.isEmpty())
            return List.of();

        Set<ResourceKey<Enchantment>> allowed = knowledges.stream().map(knowledge -> ResourceKey.create(Registries.ENCHANTMENT, knowledge.data())).collect(Collectors.toSet());
        Stream<Holder<Enchantment>> filtered = optional.get().stream().filter(holder -> holder.unwrapKey().map(allowed::contains).orElse(false));
        List<EnchantmentInstance> list = EnchantmentHelper.selectEnchantment(random, stack, j, filtered);

        if (stack.is(Items.BOOK) && list.size() > 1) {
            list.remove(random.nextInt(list.size()));
        }

        return list;
    }
}
