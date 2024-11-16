package dev.creoii.greatbigworld.swordsandshields.screen;

import com.mojang.datafixers.util.Pair;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsScreenHandlers;
import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentPlayer;
import dev.creoii.greatbigworld.swordsandshields.util.LearnEnchantment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Blocks;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Set;
import java.util.stream.Collectors;

public class EnchantmentScreenHandler extends ScreenHandler {
    static final Identifier EMPTY_LAPIS_SLOT_TEXTURE = Identifier.of("item/empty_slot_lapis_lazuli");
    private final PlayerEntity player;
    private final Inventory inventory;
    private int enchantmentsCount;
    private final ScreenHandlerContext context;
    private final Random random;
    private final Property seed;
    public int[] enchantmentPower;
    public int[] enchantmentId;
    public int[] enchantmentLevel;
    private boolean stackHasNeededEnchantments;

    public EnchantmentScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY, playerInventory.player);
    }

    public EnchantmentScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, PlayerEntity player) {
        super(SwordsAndShieldsScreenHandlers.ENCHANTMENT, syncId);
        this.player = player;
        this.inventory = new SimpleInventory(2) {
            public void markDirty() {
                super.markDirty();
                EnchantmentScreenHandler.this.onContentChanged(this);
            }
        };
        this.random = Random.create();
        this.seed = Property.create();
        this.context = context;
        this.addSlot(new Slot(this.inventory, 0, 15, 47) {
            public int getMaxItemCount() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.inventory, 1, 35, 47) {
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.LAPIS_LAZULI);
            }

            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, EMPTY_LAPIS_SLOT_TEXTURE);
            }
        });

        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        if (player instanceof EnchantmentPlayer enchantmentPlayer) {
            enchantmentsCount = Math.max(0, enchantmentPlayer.gbw$getEnchantments().size() - 1);
            this.enchantmentPower = new int[enchantmentsCount];
            this.enchantmentId = new int[enchantmentsCount];
            this.enchantmentLevel = new int[enchantmentsCount];
            //addProperty(Property.create()).set(enchantmentsCount);
            for (i = 0; i < enchantmentsCount; ++i) {
                this.addProperty(Property.create(this.enchantmentPower, i));
                this.addProperty(Property.create(this.enchantmentId, i));
                this.addProperty(Property.create(this.enchantmentLevel, i));
            }
            this.addProperty(this.seed).set(player.getEnchantmentTableSeed());
        }
    }

    public int getEnchantmentsCount() {
        return enchantmentsCount;
    }

    public boolean doesStackHaveNeededEnchantments() {
        return stackHasNeededEnchantments;
    }

    public void onContentChanged(Inventory inventory) {
        if (inventory == this.inventory) {
            ItemStack itemStack = inventory.getStack(0);
            if (!itemStack.isEmpty() && itemStack.isEnchantable()) {
                /*if (inventory instanceof PlayerInventory playerInventory) {
                    stackHasNeededEnchantments = playerInventory.player instanceof EnchantmentPlayer enchantmentPlayer && !EnchantmentHelper.getEnchantments(itemStack).getEnchantments().stream().map(RegistryEntry::value).collect(Collectors.toSet()).equals(enchantmentPlayer.gbw$getEnchantments());
                } else stackHasNeededEnchantments = false;*/
                System.out.println("contentchanged count 1: " + enchantmentsCount);
                this.context.run((world, pos) -> {
                    int bookshelfCount = /*0*/15;

                    for (BlockPos blockPos : EnchantingTableBlock.POWER_PROVIDER_OFFSETS) {
                        if (EnchantingTableBlock.canAccessPowerProvider(world, pos, blockPos)) {
                            ++bookshelfCount;
                        }
                    }

                    this.random.setSeed(this.seed.get());

                    int j;
                    System.out.println("contentchanged count 2: " + enchantmentsCount);
                    for (j = 0; j < enchantmentsCount; ++j) {
                        this.enchantmentPower[j] = EnchantmentHelper.calculateRequiredExperienceLevel(this.random, j, bookshelfCount, itemStack);
                        System.out.println("set power: " + enchantmentPower[j]);
                        this.enchantmentId[j] = -1;
                        this.enchantmentLevel[j] = -1;
                        if (this.enchantmentPower[j] < j + 1) {
                            this.enchantmentPower[j] = 0;
                        }
                    }

                    for (j = 0; j < enchantmentsCount; ++j) {
                        if (this.enchantmentPower[j] > 0 && inventory instanceof PlayerInventory playerInventory && playerInventory.player instanceof EnchantmentPlayer enchantmentPlayer) {
                            Set<EnchantmentLevelEntry> set = generateEnchantments(playerInventory.player, itemStack, j, enchantmentPower[j]);
                            if (set != null && !set.isEmpty()) {
                                for (EnchantmentLevelEntry entry : set) {
                                    this.enchantmentId[j] = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getRawId(entry.enchantment.value());
                                    this.enchantmentLevel[j] = entry.level;
                                }
                            }
                        }
                    }

                    this.sendContentUpdates();
                });
            } else {
                for (int i = 0; i < enchantmentsCount; ++i) {
                    this.enchantmentPower[i] = 0;
                    this.enchantmentId[i] = -1;
                    this.enchantmentLevel[i] = -1;
                }
            }
        }
    }

    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id >= 0 && id < this.enchantmentPower.length) {
            ItemStack itemStack = this.inventory.getStack(0);
            ItemStack itemStack2 = this.inventory.getStack(1);
            int i = id + 1;
            if ((itemStack2.isEmpty() || itemStack2.getCount() < i) && !player.isInCreativeMode()) {
                return false;
            } else if (this.enchantmentPower[id] <= 0 || itemStack.isEmpty() && !player.getAbilities().creativeMode) {
                return false;
            } else {
                this.context.run((world, pos) -> {
                    Registry<Enchantment> enchantmentRegistry = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT);

                    ItemStack itemStack3 = itemStack;
                    EnchantmentLevelEntry entry = null;
                    Enchantment enchantment = enchantmentRegistry.get(enchantmentId[id]);
                    int level = enchantmentPower[id] + 1 + random.nextInt(1) + random.nextInt(1);
                    float f = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;
                    level = MathHelper.clamp(Math.round(level + level * f), 1, Integer.MAX_VALUE);
                    if (enchantment != null)
                        entry = new EnchantmentLevelEntry(enchantmentRegistry.createEntry(enchantment), level);
                    if (entry != null) {
                        System.out.println(entry.enchantment.getKey().get());
                        player.applyEnchantmentCosts(itemStack3, i);
                        if (itemStack3.isOf(Items.BOOK)) {
                            itemStack3 = itemStack.copyComponentsToNewStack(Items.ENCHANTED_BOOK, 1);
                            this.inventory.setStack(0, itemStack3);
                        }

                        itemStack3.addEnchantment(entry.enchantment, entry.level);

                        if (!player.isInCreativeMode()) {
                            itemStack2.decrement(i);
                            if (itemStack2.isEmpty()) {
                                this.inventory.setStack(1, ItemStack.EMPTY);
                            }
                        }

                        player.incrementStat(Stats.ENCHANT_ITEM);
                        if (player instanceof ServerPlayerEntity) {
                            Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, itemStack3, i);
                        }

                        this.inventory.markDirty();
                        this.seed.set(player.getEnchantmentTableSeed());
                        this.onContentChanged(this.inventory);
                        world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                    }
                });
                return true;
            }
        } else {
            ItemStack stack = this.inventory.getStack(0).copy();
            if (id == 0 && EnchantmentHelper.hasEnchantments(stack)) {
                context.run((world, pos) -> {
                    ItemEnchantmentsComponent enchantmentsComponent = EnchantmentHelper.getEnchantments(stack);
                    if (!player.getWorld().isClient) {
                        if (player instanceof EnchantmentPlayer enchantmentPlayer) {
                            enchantmentsComponent.getEnchantments().forEach(entry -> {
                                enchantmentPlayer.gbw$addEnchantment(entry.getKey().get());
                                ServerPlayNetworking.send((ServerPlayerEntity) player, new LearnEnchantment(world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getId(entry.value())));
                            });
                        }
                    }

                    if (stack.get(DataComponentTypes.ENCHANTMENTS) != null) {
                        stack.set(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
                    } else if (stack.get(DataComponentTypes.STORED_ENCHANTMENTS) != null) {
                        stack.set(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
                    }
                    inventory.setStack(0, stack.isOf(Items.ENCHANTED_BOOK) ? Items.BOOK.getDefaultStack() : stack);
                    this.inventory.markDirty();
                    this.seed.set(player.getEnchantmentTableSeed());
                    this.onContentChanged(this.inventory);
                    world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                });
                return true;
            }
            Util.error(player.getName() + " pressed invalid button id: " + id);
            return false;
        }
    }

    private Set<EnchantmentLevelEntry> generateEnchantments(PlayerEntity player, ItemStack stack, int slot, int level) {
        MutableInt mutable = new MutableInt(level);
        this.random.setSeed(this.seed.get() + slot);
        Set<EnchantmentLevelEntry> set = ((EnchantmentPlayer) player).gbw$getEnchantments().stream().map(enchantment -> {
            mutable.add(1 + random.nextInt(1) + random.nextInt(1));
            float f = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;
            mutable.setValue(MathHelper.clamp(Math.round(mutable.floatValue() + mutable.floatValue() * f), 1, Integer.MAX_VALUE));
            return new EnchantmentLevelEntry(player.getRegistryManager().get(RegistryKeys.ENCHANTMENT).entryOf(enchantment), mutable.getValue());
        }).collect(Collectors.toSet());
        if (stack.isOf(Items.BOOK) && set.size() > 1) {
            //set.remove(this.random.nextInt(set.size()));
        }
        return set;
    }

    public int getLapisCount() {
        ItemStack itemStack = this.inventory.getStack(1);
        return itemStack.isEmpty() ? 0 : itemStack.getCount();
    }

    public int getSeed() {
        return this.seed.get();
    }

    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> {
            this.dropInventory(player, this.inventory);
        });
    }

    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, Blocks.ENCHANTING_TABLE);
    }

    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == 0) {
                if (!this.insertItem(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slot == 1) {
                if (!this.insertItem(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemStack2.isOf(Items.LAPIS_LAZULI)) {
                if (!this.insertItem(itemStack2, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (this.slots.getFirst().hasStack() || !this.slots.getFirst().canInsert(itemStack2)) {
                    return ItemStack.EMPTY;
                }

                ItemStack itemStack3 = itemStack2.copyWithCount(1);
                itemStack2.decrement(1);
                this.slots.getFirst().setStack(itemStack3);
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot2.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }
}