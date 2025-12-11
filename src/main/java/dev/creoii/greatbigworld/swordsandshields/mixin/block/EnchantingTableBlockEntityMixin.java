package dev.creoii.greatbigworld.swordsandshields.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.client.GreatBigWorldClient;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;

@Mixin(EnchantingTableBlockEntity.class)
public class EnchantingTableBlockEntityMixin {
    @WrapOperation(method = "bookAnimationTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getNearestPlayer(DDDDZ)Lnet/minecraft/world/entity/player/Player;"))
    private static Player gbw$ignoreKnowledgelessPlayers(Level instance, double x, double y, double z, double v, boolean b, Operation<Player> original) {
        Player player = original.call(instance, x, y, z, v, b);

        Set<Knowledge> knowledge = GreatBigWorldClient.getKnowledge().get(Knowledge.Type.ENCHANTMENT);
        if (knowledge == null || knowledge.isEmpty()) {
            return null;
        }

        return player;
    }
}
