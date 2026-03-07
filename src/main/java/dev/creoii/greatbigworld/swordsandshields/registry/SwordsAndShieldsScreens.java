package dev.creoii.greatbigworld.swordsandshields.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.swordsandshields.client.CustomEnchantmentScreen;
import dev.creoii.greatbigworld.swordsandshields.menu.CustomEnchantmentMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class SwordsAndShieldsScreens {
    public static final MenuType<CustomEnchantmentMenu> ENCHANTING = new MenuType<>(CustomEnchantmentMenu::new, FeatureFlags.VANILLA_SET);

    public static void register() {
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "enchanting"), ENCHANTING);
    }

    public static void registerClient() {
        MenuScreens.register(ENCHANTING, CustomEnchantmentScreen::new);
    }
}
