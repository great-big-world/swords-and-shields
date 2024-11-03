package dev.creoii.greatbigworld.swordsandshields.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.swordsandshields.SwordsAndShields;
import dev.creoii.greatbigworld.swordsandshields.client.EnchantmentScreen;
import dev.creoii.greatbigworld.swordsandshields.screen.EnchantmentScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public final class SwordsAndShieldsScreenHandlers {
    public static final ScreenHandlerType<EnchantmentScreenHandler> ENCHANTMENT = new ScreenHandlerType<>(EnchantmentScreenHandler::new, FeatureFlags.VANILLA_FEATURES);

    public static void register() {
        Registry.register(Registries.SCREEN_HANDLER, Identifier.of(GreatBigWorld.NAMESPACE, "enchantment"), ENCHANTMENT);
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        HandledScreens.register(SwordsAndShieldsScreenHandlers.ENCHANTMENT, EnchantmentScreen::new);
    }
}
