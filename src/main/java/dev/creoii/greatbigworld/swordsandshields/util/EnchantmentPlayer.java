package dev.creoii.greatbigworld.swordsandshields.util;

import dev.creoii.greatbigworld.swordsandshields.block.EnchantedStoneBlockEntity;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsBlocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;

import java.util.Set;

public interface EnchantmentPlayer {
    boolean gbw$addEnchantment(Enchantment enchantment);

    Set<Enchantment> gbw$getEnchantments();

    static void addEnchantedStone(StructureWorldAccess world, BlockPos pos, Enchantment enchantment, BlockBox boundingBox) {
        if (boundingBox.contains(pos)) {
            world.setBlockState(pos, SwordsAndShieldsBlocks.ENCHANTED_STONE.getDefaultState(), 2);
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof EnchantedStoneBlockEntity enchantedStoneBlockEntity) {
                enchantedStoneBlockEntity.setEnchantment(enchantment);
            }
        }
    }
}
