package dev.creoii.greatbigworld.swordsandshields.mixin.block;

import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsDamageTypes;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BambooBlock.class)
public class BambooBlockMixin {
    @Inject(method = "updateLeaves", at = @At("TAIL"))
    private void gbw$damageOnGrow(BlockState state, World world, BlockPos pos, Random random, int height, CallbackInfo ci) {
        if (world.isClient())
            return;

        final Box box = new Box(pos.up());
        for (Entity entity : world.getOtherEntities(null, box, Entity::isAlive)) {
            entity.damage((ServerWorld) world, world.getDamageSources().create(SwordsAndShieldsDamageTypes.BAMBOO), 1f);
        }
    }
}
