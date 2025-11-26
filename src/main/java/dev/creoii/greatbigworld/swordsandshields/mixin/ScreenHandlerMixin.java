package dev.creoii.greatbigworld.swordsandshields.mixin;

import dev.creoii.greatbigworld.swordsandshields.util.ExtendedScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin implements ExtendedScreenHandler {
    @Unique
    private PlayerEntity gbw$player;

    @Override
    public PlayerEntity gbw$getPlayer() {
        return gbw$player;
    }

    @Override
    public void gbw$setPlayer(PlayerEntity player) {
        this.gbw$player = player;
    }
}
