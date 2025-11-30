package dev.creoii.greatbigworld.swordsandshields.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.client.GreatBigWorldClient;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import net.minecraft.block.entity.EnchantingTableBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@Mixin(EnchantingTableBlockEntity.class)
public class EnchantingTableBlockEntityMixin {
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getClosestPlayer(DDDDZ)Lnet/minecraft/entity/player/PlayerEntity;"))
    private static PlayerEntity gbw$ignoreKnowledgelessPlayers(World instance, double x, double y, double z, double v, boolean b, Operation<PlayerEntity> original) {
        PlayerEntity player = original.call(instance, x, y, z, v, b);

        Set<Knowledge> knowledge = GreatBigWorldClient.getKnowledge().get(Knowledge.Type.ENCHANTMENT);
        if (knowledge == null || knowledge.isEmpty()) {
            return null;
        }

        return player;
    }
}
