package dev.creoii.greatbigworld.swordsandshields.client;

import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldBlocks;
import dev.creoii.greatbigworld.swordsandshields.util.DynamicHudPlayer;
import dev.creoii.greatbigworld.swordsandshields.util.LearnEnchantment;
import dev.creoii.greatbigworld.swordsandshields.util.SyncStatusHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class SwordsAndShieldsClient implements ClientModInitializer {
    @Nullable
    private static Enchantment currentEnchantment = null;
    private static int learnEnchantmentTime = -1;

    @Override
    public void onInitializeClient() {
        SwordsAndShieldBlocks.registerClient();

        ClientPlayNetworking.registerGlobalReceiver(SyncStatusHud.PACKET_ID, (payload, context) -> {
            SyncStatusHud.Type type = payload.type();
            context.client().execute(() -> {
                if (context.player() instanceof DynamicHudPlayer dynamicHudPlayer) {
                    switch (type) {
                        case FOOD -> dynamicHudPlayer.gbw$resetHideFoodHud();
                        case ARMOR -> dynamicHudPlayer.gbw$resetHideArmorHud();
                        case HEALTH -> dynamicHudPlayer.gbw$resetHideHealthHud();
                        case EXPERIENCE -> dynamicHudPlayer.gbw$resetHideExpHud();
                    }
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(LearnEnchantment.PACKET_ID, (payload, context) -> {
            Identifier enchantment = payload.enchantment();
            context.client().execute(() -> {
                currentEnchantment = Registries.ENCHANTMENT.get(enchantment);
                learnEnchantmentTime = 160; // 8 seconds
            });
        });
    }

    public static void renderLearnEnchantmentOverlay(MinecraftClient client, DrawContext context, float tickDelta) {
        if (learnEnchantmentTime >= 0 && currentEnchantment != null) {
            Text text = Text.translatable("gui.learnEnchantment", Text.translatable(currentEnchantment.getTranslationKey()));
            int opacity = learnEnchantmentTime > 80 ? 255 : (int) (255 * (learnEnchantmentTime / 80d));

            context.drawText(client.textRenderer, text, client.textRenderer.getWidth(text) / 2, 10, subtractOpacity(0xffffff, opacity), false);
            --learnEnchantmentTime;
        }

        if (learnEnchantmentTime < 0 && currentEnchantment != null) {
            currentEnchantment = null;
        }
    }

    private static int subtractOpacity(int color, int subtract) {
        return (subtract << 24) | (color & 0x00ffffff);
    }
}
