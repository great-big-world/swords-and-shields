package dev.creoii.greatbigworld.swordsandshields.mixin.block;

import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BambooStalkBlock.class)
public class BambooBlockMixin {
    @Inject(method = "growBamboo", at = @At("TAIL"))
    private void gbw$damageOnGrow(BlockState state, Level world, BlockPos pos, RandomSource random, int height, CallbackInfo ci) {
        if (world.isClientSide())
            return;

        final AABB box = new AABB(pos.above());
        for (Entity entity : world.getEntities((Entity) null, box, Entity::isAlive)) {
            entity.hurtServer((ServerLevel) world, world.damageSources().source(SwordsAndShieldsDamageTypes.BAMBOO), 1f);
        }
    }
}
