package dev.creoii.greatbigworld.swordsandshields.mixin.menu;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.client.GreatBigWorldClient;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsTrimMaterials;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsTrimPatterns;
import dev.creoii.greatbigworld.swordsandshields.util.ExtendedAnvilMenu;
import dev.creoii.greatbigworld.swordsandshields.util.ExtendedScreenHandler;
import dev.creoii.greatbigworld.swordsandshields.util.SwordsAndShieldsTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu implements ExtendedAnvilMenu {
    @Shadow private boolean onlyRenaming;
    @Shadow public abstract void createResult();

    @Unique private Set<TrimPattern> knownPatterns;
    @Unique Runnable slotUpdateListener;
    @Unique DataSlot selectedRecipeIndex;
    @Unique private boolean fixRename;

    public AnvilMenuMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, ItemCombinerMenuSlotDefinition itemCombinerMenuSlotDefinition) {
        super(menuType, i, inventory, containerLevelAccess, itemCombinerMenuSlotDefinition);
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    private void gbw$init(int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, CallbackInfo ci) {
        knownPatterns = new HashSet<>();
        slotUpdateListener = () -> {
        };
        selectedRecipeIndex = DataSlot.standalone();
        selectedRecipeIndex.set(-1);
        fixRename = false;
    }

    @WrapOperation(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;hasInfiniteMaterials()Z"))
    private boolean gbw$dontTakeLevels(Player instance, Operation<Boolean> original) {
        return true;
    }

    @WrapOperation(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 2))
    private void gbw$fixDecrementDyes2(Container instance, int i, ItemStack stack, Operation<Void> original) {
        if (this instanceof ExtendedScreenHandler extendedScreenHandler && extendedScreenHandler.gbw$getPlayer().hasInfiniteMaterials())
            return;

        ItemStack stack1 = inputSlots.getItem(1);
        ItemStack temp = stack1.copy();
        if (fixRename && stack1.getCount() > 1) {
            stack1.shrink(1);
        } else original.call(instance, i, stack);

        if (!temp.equals(stack1))
            fixRename = false;
    }

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    private void gbw$allowPickupAlways(Player player, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @WrapOperation(method = "setItemName", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;"))
    private MutableComponent gbw$colorItemName(String string, Operation<MutableComponent> original) {
        ItemStack stack = inputSlots.getItem(1);
        if (stack.getItem() instanceof DyeItem dyeItem) {
            return original.call(string).withColor(dyeItem.getDyeColor().getTextColor());
        }
        return original.call(string);
    }

    @WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0))
    private boolean gbw$allowDyeMatch(ItemStack instance, Item item, Operation<Boolean> original, @Local(ordinal = 2) ItemStack itemStack3) {
        return original.call(instance, item) || isValidTrimMaterialItem(itemStack3);
    }

    @WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z", ordinal = 1))
    private boolean gbw$allowDyeNonRenamedItems(ItemStack instance, DataComponentType dataComponentType, Operation<Boolean> original, @Local(ordinal = 2) ItemStack itemStack3) {
        return original.call(instance, dataComponentType) || isValidTrimMaterialItem(itemStack3);
    }

    @WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z", ordinal = 2))
    private boolean gbw$cancelDamageLogic(ItemStack instance, Operation<Boolean> original) {
        return false;
    }

    @WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 4))
    private void gbw$simpleDamageLogic(ResultContainer instance, int i, ItemStack stack, Operation<Void> original, @Local(ordinal = 0) ItemStack itemStack) {
        stack.setDamageValue(itemStack.getDamageValue());
        original.call(instance, i, stack);
    }

    @WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 4))
    private void gbw$colorItem(ResultContainer instance, int i, ItemStack itemStack, Operation<Void> original) {
        ItemStack itemStack3 = inputSlots.getItem(1);
        if (isValidTrimMaterialItem(itemStack3)) {
            Player player = ((ExtendedScreenHandler) this).gbw$getPlayer();
            Level level = player.level();

            Registry<TrimPattern> patternRegistry = level.registryAccess().lookupOrThrow(Registries.TRIM_PATTERN);
            Registry<TrimMaterial> materialRegistry = level.registryAccess().lookupOrThrow(Registries.TRIM_MATERIAL);

            boolean hasTrim = itemStack3.has(DataComponents.PROVIDES_TRIM_MATERIAL);

            if (gbw$getSelectedRecipeIndex() == -1) {
                if (hasTrim) {
                    Holder<TrimMaterial> material = materialRegistry.get(itemStack3.get(DataComponents.PROVIDES_TRIM_MATERIAL).material().key().get()).orElseThrow();
                    boolean specialTrim = hasTrim && material.is(SwordsAndShieldsTags.DECORATION);
                    if (specialTrim && canApply(material, itemStack)) {
                        itemStack.set(DataComponents.TRIM, new ArmorTrim(material, patternRegistry.getOrThrow(SwordsAndShieldsTrimPatterns.NONE)));
                        original.call(instance, i, itemStack);
                        onlyRenaming = false;
                        fixRename = true;
                    }
                    return;
                }
                Component oldName = itemStack.get(DataComponents.CUSTOM_NAME);
                if (oldName == null) {
                    oldName = itemStack.getItemName().copy();
                }
                if (itemStack3.getItem() instanceof DyeItem dyeItem)
                    itemStack.set(DataComponents.CUSTOM_NAME, oldName.copy().withColor(dyeItem.getDyeColor().getTextColor()));
                original.call(instance, i, itemStack);
                onlyRenaming = false;
                fixRename = true;
                return;
            } else if (hasTrim) {
                ResourceKey<TrimMaterial> material = itemStack3.get(DataComponents.PROVIDES_TRIM_MATERIAL).material().key().get();

                if (!gbw$getKnownPatterns().isEmpty() && isValidPatternIndex(gbw$getSelectedRecipeIndex())) {
                    TrimPattern[] trimPatterns = gbw$getKnownPatterns().toArray(TrimPattern[]::new);
                    TrimPattern pattern = trimPatterns[gbw$getSelectedRecipeIndex()];
                    itemStack.set(DataComponents.TRIM, new ArmorTrim(materialRegistry.get(material).get(), patternRegistry.getOrThrow(patternRegistry.getResourceKey(pattern).get())));
                }

                original.call(instance, i, itemStack);
                onlyRenaming = false;
                fixRename = true;
                return;
            }
        }
        original.call(instance, i, itemStack);
    }

    @WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z"))
    private boolean gbw$allowDyeAlways(String instance, Object o, Operation<Boolean> original, @Local(ordinal = 2) ItemStack itemStack3) {
        return original.call(instance, o) && !(isValidTrimMaterialItem(itemStack3));
    }

    @ModifyConstant(method = "createInputSlotDefinitions", constant = @Constant(intValue = 47, ordinal = 0))
    private static int gbw$modifySlotY1(int constant) {
        return 46;
    }

    @ModifyConstant(method = "createInputSlotDefinitions", constant = @Constant(intValue = 47, ordinal = 1))
    private static int gbw$modifySlotY2(int constant) {
        return 46;
    }

    @ModifyConstant(method = "createInputSlotDefinitions", constant = @Constant(intValue = 47, ordinal = 2))
    private static int gbw$modifySlotY3(int constant) {
        return 83;
    }

    @ModifyConstant(method = "createInputSlotDefinitions", constant = @Constant(intValue = 27))
    private static int gbw$modifySlotX1(int constant) {
        return 22;
    }

    @ModifyConstant(method = "createInputSlotDefinitions", constant = @Constant(intValue = 76))
    private static int gbw$modifySlotX2(int constant) {
        return 49;
    }

    @ModifyConstant(method = "createInputSlotDefinitions", constant = @Constant(intValue = 134))
    private static int gbw$modifySlotX3(int constant) {
        return 36;
    }

    @Unique
    private void setupTrimPatterns(ItemStack itemStack) {
        knownPatterns = new HashSet<>();
        if (!itemStack.isEmpty()) {
            Player player = ((ExtendedScreenHandler) this).gbw$getPlayer();
            Level level = player.level();

            Registry<TrimPattern> registry = level.registryAccess().lookupOrThrow(Registries.TRIM_PATTERN);

            if (level.isClientSide()) {
                Set<Knowledge> knowledge = GreatBigWorldClient.getKnowledge().get(Knowledge.Type.ARMOR_TRIM);
                if (knowledge == null)
                    return;

                knowledge.forEach(knowledge1 -> {
                    knownPatterns.add(registry.getValue(knowledge1.data()));
                });
            } else {
                KnowledgeManager manager = KnowledgeManager.getServerState(level.getServer());
                Set<Knowledge> knowledge = manager.getPlayerKnowledge(player, Knowledge.Type.ARMOR_TRIM);
                if (knowledge == null)
                    return;

                knowledge.forEach(knowledge1 -> {
                    knownPatterns.add(registry.getValue(knowledge1.data()));
                });
            }
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int i) {
        if (selectedRecipeIndex.get() == i) {
            selectedRecipeIndex.set(-1);
            createResult();
        } else {
            if (isValidPatternIndex(i)) {
                selectedRecipeIndex.set(i);
                createResult();
            }
        }
        return true;
    }

    @Unique
    public Set<TrimPattern> gbw$getKnownPatterns() {
        return knownPatterns;
    }

    @Override
    public int gbw$getNumberOfVisiblePatterns() {
        return knownPatterns.size();
    }

    @Override
    public void gbw$registerUpdateListener(Runnable runnable) {
        slotUpdateListener = runnable;
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        slotUpdateListener.run();
        setupTrimPatterns(inputSlots.getItem(0));
    }

    @Override
    public int gbw$getSelectedRecipeIndex() {
        return selectedRecipeIndex.get();
    }

    @Unique
    public boolean gbw$hasInputItem() {
        return !inputSlots.getItem(0).isEmpty() && !knownPatterns.isEmpty();
    }

    @Unique
    private boolean isValidPatternIndex(int i) {
        return i >= 0 && i < knownPatterns.size();
    }

    @Unique
    private static boolean isValidTrimMaterialItem(ItemStack stack) {
        return stack.getItem() instanceof DyeItem || stack.is(SwordsAndShieldsTags.ARMOR_DECORATION_ITEMS);
    }

    @Unique
    private boolean canApply(Holder<TrimMaterial> material, ItemStack itemStack) {
        return material.is(SwordsAndShieldsTags.DECORATION) && itemStack.has(DataComponents.EQUIPPABLE) && itemStack.get(DataComponents.EQUIPPABLE).slot() == EquipmentSlot.HEAD;
    }
}
