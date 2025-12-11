package dev.creoii.greatbigworld.swordsandshields.mixin.block;

import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BambooSaplingBlock;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BambooSaplingBlock.class)
public class BambooShootBlockMixin {
    @Inject(method = "growBamboo(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V", at = @At("TAIL"))
    private void gbw$damageOnGrow(Level world, BlockPos pos, CallbackInfo ci) {
        if (world.isClientSide())
            return;

        final AABB box = new AABB(pos.above());
        for (Entity entity : world.getEntities((Entity) null, box, Entity::isAlive)) {
            entity.hurtServer((ServerLevel) world, world.damageSources().source(SwordsAndShieldsDamageTypes.BAMBOO), 1f);
        }
    }
}
