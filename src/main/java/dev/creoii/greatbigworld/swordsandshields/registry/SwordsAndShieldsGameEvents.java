package dev.creoii.greatbigworld.swordsandshields.registry;

import dev.creoii.greatbigworld.swordsandshields.SwordsAndShields;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.event.GameEvent;

public final class SwordsAndShieldsGameEvents {
    public static RegistryEntry<GameEvent> LEARN_ENCHANTMENT;

    public static void register() {
        LEARN_ENCHANTMENT = Registry.registerReference(Registries.GAME_EVENT, new Identifier(SwordsAndShields.NAMESPACE, "learn_enchantment"), new GameEvent(16));
    }
}
