package dev.creoii.greatbigworld.swordsandshields.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.swordsandshields.screen.EnchantmentScreenHandler;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantingTableBlock.class)
public class EnchantingTableBlockMixin {
    @ModifyReturnValue(method = "method_17467", at = @At("RETURN"))
    private static ScreenHandler gbw$useImprovedEnchantingScreen(ScreenHandler original, @Local(ordinal = 0, argsOnly = true) int syncId, @Local(argsOnly = true) PlayerInventory inventory, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) PlayerEntity player) {
        return new EnchantmentScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos), player);
    }
}
