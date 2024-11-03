package dev.creoii.greatbigworld.swordsandshields.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.*;

public class EnchantmentManager extends PersistentState {
    private static final Type<EnchantmentManager> TYPE = new Type<>(EnchantmentManager::new, EnchantmentManager::createFromNbt, null);
    public HashMap<UUID, Long> players = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound playersNbt = new NbtCompound();
        players.forEach((uuid, enchantments) -> {
            playersNbt.putLong(uuid.toString(), enchantments);
        });
        nbt.put("known_enchantments", playersNbt);
        return nbt;
    }

    public static EnchantmentManager createFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        EnchantmentManager state = new EnchantmentManager();
        NbtCompound players = nbt.getCompound("known_enchantments");
        players.getKeys().forEach(key -> {
            state.players.put(UUID.fromString(key), players.getLong(key));
        });
        return state;
    }

    public static EnchantmentManager getServerState(MinecraftServer server) {
        PersistentStateManager stateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        EnchantmentManager manager = stateManager.getOrCreate(TYPE, "gbw_known_enchantments");
        manager.markDirty();
        return manager;
    }

    public static long getPlayerKnownEnchantments(PlayerEntity player) {
        EnchantmentManager serverState = getServerState(player.getWorld().getServer());
        return serverState.players.computeIfAbsent(player.getUuid(), uuid -> -1L);
    }

    public static long writeEnchantments(DynamicRegistryManager.Immutable registryManager, Set<RegistryKey<Enchantment>> enchantmentsSet) {
        if (enchantmentsSet.isEmpty())
            return -1;

        Registry<Enchantment> enchantmentRegistry = registryManager.get(RegistryKeys.ENCHANTMENT);
        long encodedEnchantments = 0;
        int i = 0;
        for (RegistryKey<Enchantment> enchantment : enchantmentsSet) {
            encodedEnchantments += (long) (enchantmentRegistry.getRawId(enchantmentRegistry.get(enchantment)) * Math.pow(enchantmentRegistry.size(), i));
            ++i;
        }

        return encodedEnchantments;
    }

    public static Set<RegistryKey<Enchantment>> readEnchantments(DynamicRegistryManager.Immutable registryManager, long enchantments) {
        Registry<Enchantment> enchantmentRegistry = registryManager.get(RegistryKeys.ENCHANTMENT);

        Set<RegistryKey<Enchantment>> enchantmentsSet = new HashSet<>();
        while (enchantments > 0) {
            Enchantment enchantment = enchantmentRegistry.get((int) (enchantments % enchantmentRegistry.size()));
            if (enchantment != null) {
                enchantmentsSet.add(enchantmentRegistry.getKey(enchantment).get());
                enchantments /= enchantmentRegistry.size();
            }
        }
        return enchantmentsSet;
    }
}
