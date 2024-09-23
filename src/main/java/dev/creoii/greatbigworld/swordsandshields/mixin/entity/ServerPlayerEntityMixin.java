package dev.creoii.greatbigworld.swordsandshields.mixin.entity;

import com.mojang.authlib.GameProfile;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsGameEvents;
import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentPlayer;
import dev.creoii.greatbigworld.swordsandshields.util.LearnEnchantment;
import dev.creoii.greatbigworld.swordsandshields.util.SyncStatusHud;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    @Mutable
    @Shadow @Final private ScreenHandlerListener screenHandlerListener;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "changeGameMode", at = @At("RETURN"))
    private void gbw$resetStatusHudOnSurvival(GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
        if (gameMode == GameMode.SURVIVAL) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) (Object) this;
            ServerPlayNetworking.send(serverPlayer, new SyncStatusHud(SyncStatusHud.Type.FOOD));
            ServerPlayNetworking.send(serverPlayer, new SyncStatusHud(SyncStatusHud.Type.HEALTH));
            ServerPlayNetworking.send(serverPlayer, new SyncStatusHud(SyncStatusHud.Type.ARMOR));
            ServerPlayNetworking.send(serverPlayer, new SyncStatusHud(SyncStatusHud.Type.EXPERIENCE));
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$fixScreenHandlerListener(MinecraftServer server, ServerWorld world, GameProfile profile, SyncedClientOptions clientOptions, CallbackInfo ci) {
        screenHandlerListener = new ScreenHandlerListener() {
            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
                Slot slot = handler.getSlot(slotId);
                if (!(slot instanceof CraftingResultSlot)) {
                    if (slot.inventory == ServerPlayerEntityMixin.this.getInventory()) {
                        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) (Object) ServerPlayerEntityMixin.this;
                        Criteria.INVENTORY_CHANGED.trigger(serverPlayer, ServerPlayerEntityMixin.this.getInventory(), stack);

                        if (!getWorld().isClient && serverPlayer instanceof EnchantmentPlayer enchantmentPlayer) {
                            ItemEnchantmentsComponent itemEnchantments = stack.getEnchantments();
                            if (itemEnchantments.isEmpty() && stack.get(DataComponentTypes.STORED_ENCHANTMENTS) != null) {
                                itemEnchantments = stack.get(DataComponentTypes.STORED_ENCHANTMENTS);
                            }

                            if (itemEnchantments != null) {
                                itemEnchantments.getEnchantments().forEach(entry -> {
                                    if (enchantmentPlayer.gbw$addEnchantment(entry.value()) && entry.getKey().isPresent()) {
                                        ServerPlayNetworking.send(serverPlayer, new LearnEnchantment(entry.getKey().get().getValue()));
                                        world.emitGameEvent(SwordsAndShieldsGameEvents.LEARN_ENCHANTMENT, getPos(), GameEvent.Emitter.of(serverPlayer));
                                        world.playSound(serverPlayer, getBlockPos(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1f, 1f);
                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {}
        };
    }
}
