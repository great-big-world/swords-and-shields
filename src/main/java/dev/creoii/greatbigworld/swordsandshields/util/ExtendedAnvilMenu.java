package dev.creoii.greatbigworld.swordsandshields.util;

import net.minecraft.world.item.equipment.trim.TrimPattern;

import java.util.Set;

public interface ExtendedAnvilMenu {
    int gbw$getNumberOfVisiblePatterns();

    void gbw$registerUpdateListener(Runnable runnable);

    boolean gbw$hasInputItem();

    Set<TrimPattern> gbw$getKnownPatterns();

    int gbw$getSelectedRecipeIndex();
}
