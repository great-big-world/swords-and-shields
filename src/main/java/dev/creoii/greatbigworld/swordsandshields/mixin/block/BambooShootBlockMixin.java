package dev.creoii.greatbigworld.swordsandshields.mixin.block;

import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsDamageTypes;
import net.minecraft.block.BambooShootBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BambooShootBlock.class)
public class BambooShootBlockMixin {
    @Inject(method = "grow(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V", at = @At("TAIL"))
    private void gbw$damageOnGrow(World world, BlockPos pos, CallbackInfo ci) {
        if (world.isClient())
            return;

        final Box box = new Box(pos.up());
        for (Entity entity : world.getOtherEntities(null, box, Entity::isAlive)) {
            entity.damage((ServerWorld) world, world.getDamageSources().create(SwordsAndShieldsDamageTypes.BAMBOO), 1f);
        }
    }
}
