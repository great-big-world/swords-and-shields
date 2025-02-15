package dev.creoii.greatbigworld.swordsandshields.mixin.entity;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @ModifyConstant(method = "getExperienceToDrop", constant = @Constant(intValue = 7))
    private int gbw$dropMoreXp(int constant) {
        return 12;
    }

    @ModifyConstant(method = "getExperienceToDrop", constant = @Constant(intValue = 100))
    private int gbw$allowMoreXpToDrop(int constant) {
        return 170;
    }
}
