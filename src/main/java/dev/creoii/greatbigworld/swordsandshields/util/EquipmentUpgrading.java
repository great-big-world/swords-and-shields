package dev.creoii.greatbigworld.swordsandshields.util;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsDataComponentTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class EquipmentUpgrading {
    public static final Map<Item, Function<ItemStack, ItemStack>> EQUIPMENT_UPGRADES = new HashMap<>();
    public static StreamCodec<RegistryFriendlyByteBuf, Integer> UPGRADE_PACKET_CODEC = StreamCodec.ofMember((value, buf) -> buf.writeInt(value), FriendlyByteBuf::readInt);

    public static void register() {
        registerUpgrade(Items.IRON_INGOT, stack -> upgradeDurability(stack, 5));
        registerUpgrade(Items.COPPER_INGOT, stack -> upgradeKnockback(stack, .5d));
        registerUpgrade(Items.GOLD_INGOT, stack -> upgradeEnchantability(stack, 3));
        registerUpgrade(Items.NETHERITE_INGOT, stack -> upgradeKnockbackResistance(stack, .1d));
    }

    private static ItemStack upgradeDurability(ItemStack stack, int amount) {
        ItemStack stack1 = stack.copy();
        stack1.set(DataComponents.MAX_DAMAGE, stack.getMaxDamage() + amount);
        return stack1;
    }

    private static ItemStack upgradeKnockback(ItemStack stack, double amount) {
        ItemStack stack1 = stack.copy();
        if (stack1.has(DataComponents.EQUIPPABLE)) {
            ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
            EquipmentSlot slot = stack1.get(DataComponents.EQUIPPABLE).slot();
            builder.add(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "copper_equipment_upgrade"), amount, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(slot));
        }
        return stack1;
    }

    private static ItemStack upgradeEnchantability(ItemStack stack, int amount) {
        ItemStack stack1 = stack.copy();
        if (stack.isEnchantable())
            stack1.set(DataComponents.ENCHANTABLE, new Enchantable(stack.get(DataComponents.ENCHANTABLE).value() + amount));
        return stack1;
    }

    private static ItemStack upgradeKnockbackResistance(ItemStack stack, double amount) {
        ItemStack stack1 = stack.copy();
        if (stack1.has(DataComponents.EQUIPPABLE)) {
            ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
            EquipmentSlot slot = stack1.get(DataComponents.EQUIPPABLE).slot();
            builder.add(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "netherite_equipment_upgrade"), amount, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(slot));
        }
        return stack1;
    }

    private static void registerUpgrade(Item item, Function<ItemStack, ItemStack> upgrade) {
        EQUIPMENT_UPGRADES.put(item, upgrade);
    }

    public static ItemStack getUpgradeFor(ItemStack stack, Item item) {
        if (EQUIPMENT_UPGRADES.containsKey(item))
            return EQUIPMENT_UPGRADES.get(item).apply(stack);
        return stack;
    }

    public static boolean canUpgrade(ItemStack stack) {
        if (stack.has(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES) && stack.has(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL)) {
            String material = stack.get(SwordsAndShieldsDataComponentTypes.EQUIPMENT_MATERIAL);
            int upgrades = stack.get(SwordsAndShieldsDataComponentTypes.EQUIPMENT_UPGRADES);
            int maxUpgrades = stack.has(DataComponents.TOOL) ? EquipmentMaterialUtil.TOOL_ENTRIES.get(EquipmentMaterialUtil.getToolMaterial(material)).maxUpgrades() : EquipmentMaterialUtil.ARMOR_ENTRIES.get(EquipmentMaterialUtil.getArmorMaterial(material)).maxUpgrades();
            return upgrades >= 0 && upgrades < maxUpgrades;
        }
        return false;
    }
}
