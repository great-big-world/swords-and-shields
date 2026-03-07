package dev.creoii.greatbigworld.swordsandshields.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.swordsandshields.menu.CustomEnchantmentMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantingTableBlock.class)
public class EnchantingTableBlockMixin {
    @Inject(method = "getMenuProvider", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private void gbw$redirectToCustomEnchantingScreen(BlockState blockState, Level level, BlockPos blockPos, CallbackInfoReturnable<MenuProvider> cir, @Local Component component) {
        cir.setReturnValue(new SimpleMenuProvider((i, inventory, player) -> new CustomEnchantmentMenu(i, inventory, ContainerLevelAccess.create(level, blockPos)), component));
    }
}
