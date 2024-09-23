package dev.creoii.greatbigworld.swordsandshields;

import dev.creoii.creoapi.api.event.entity.LivingEntityEvents;
import dev.creoii.creoapi.api.modification.BlockModification;
import dev.creoii.greatbigworld.swordsandshields.enchantment.EnchantmentManager;
import dev.creoii.greatbigworld.swordsandshields.registry.*;
import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentPlayer;
import dev.creoii.greatbigworld.swordsandshields.util.LearnEnchantment;
import dev.creoii.greatbigworld.swordsandshields.util.SyncStatusHud;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

// change enchanted stone to chiseled stones
// use nbt files to place enchanted stone as an actual structure piece
// add one-two more 'glow' levels
public class SwordsAndShields implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";

    @Override
    public void onInitialize() {
        SwordsAndShieldsBlocks.register();
        SwordsAndShieldsBlockEntities.register();
        SwordsAndShieldsItems.register();
        SwordsAndShieldsScreenHandlers.register();
        SwordsAndShieldsGameEvents.register();
        SwordsAndShieldsCriteria.register();

        PayloadTypeRegistry.playS2C().register(SyncStatusHud.PACKET_ID, SyncStatusHud.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(LearnEnchantment.PACKET_ID, LearnEnchantment.PACKET_CODEC);

        LivingEntityEvents.EQUIP_STACK.register((livingEntity, slot, oldStack, newStack) -> {
            if (!livingEntity.getWorld().isClient) {
                if (livingEntity instanceof ServerPlayerEntity serverPlayer && serverPlayer.interactionManager != null && serverPlayer.networkHandler != null) {
                    ServerPlayNetworking.send(serverPlayer, new SyncStatusHud(SyncStatusHud.Type.ARMOR));
                }
            }
            return true;
        });
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient) {
                if (player instanceof ServerPlayerEntity serverPlayer && serverPlayer.interactionManager != null && serverPlayer.networkHandler != null) {
                    ServerPlayNetworking.send(serverPlayer, new SyncStatusHud(SyncStatusHud.Type.HEALTH));
                }
            }
            return ActionResult.PASS;
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (handler.player instanceof EnchantmentPlayer enchantmentPlayer) {
                EnchantmentManager manager = EnchantmentManager.getServerState(server);
                if (manager.players.containsKey(handler.player.getUuid())) {
                    for (Enchantment enchantment : EnchantmentManager.readEnchantments(manager.players.get(handler.player.getUuid()))) {
                        enchantmentPlayer.gbw$addEnchantment(enchantment);
                    }
                }
            }
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            if (handler.player instanceof EnchantmentPlayer enchantmentPlayer && !enchantmentPlayer.gbw$getEnchantments().isEmpty()) {
                EnchantmentManager manager = EnchantmentManager.getServerState(server);
                manager.players.put(handler.player.getUuid(), EnchantmentManager.writeEnchantments(enchantmentPlayer.gbw$getEnchantments()));
            }
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof EnchantmentPlayer enchantmentPlayer) {
                enchantmentPlayer.gbw$getEnchantments().clear();
            }
        });

        BlockModification.INSTANCE.setHardness(Blocks.ENCHANTING_TABLE, 15f);
    }
}
