package dev.creoii.greatbigworld.swordsandshields.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Blocks.class)
public abstract class BlocksMixin {
    @Inject(method = "ocelotOrParrot", at = @At("HEAD"), cancellable = true)
    private static void gbw$creepersSpawnOnLeaves(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type, CallbackInfoReturnable<Boolean> cir) {
        if (type == EntityType.CREEPER)
            cir.setReturnValue(true);
    }
}
