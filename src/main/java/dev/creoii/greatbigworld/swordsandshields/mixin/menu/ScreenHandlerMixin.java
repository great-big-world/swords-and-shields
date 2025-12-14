package dev.creoii.greatbigworld.swordsandshields.mixin.menu;

import dev.creoii.greatbigworld.swordsandshields.util.ExtendedScreenHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractContainerMenu.class)
public class ScreenHandlerMixin implements ExtendedScreenHandler {
    @Unique
    private Player gbw$player;

    @Override
    public Player gbw$getPlayer() {
        return gbw$player;
    }

    @Override
    public void gbw$setPlayer(Player player) {
        this.gbw$player = player;
    }
}
