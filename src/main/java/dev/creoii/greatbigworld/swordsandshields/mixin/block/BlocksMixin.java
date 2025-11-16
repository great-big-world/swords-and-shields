package dev.creoii.greatbigworld.swordsandshields.mixin.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Blocks.class)
public abstract class BlocksMixin {
    @Inject(method = "canSpawnOnLeaves", at = @At("HEAD"), cancellable = true)
    private static void gbw$creepersSpawnOnLeaves(BlockState state, BlockView world, BlockPos pos, EntityType<?> type, CallbackInfoReturnable<Boolean> cir) {
        if (type == EntityType.CREEPER)
            cir.setReturnValue(true);
    }
}
