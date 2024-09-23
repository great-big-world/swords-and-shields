package dev.creoii.greatbigworld.swordsandshields.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.*;

public class EnchantmentManager extends PersistentState {
    private static final Type<EnchantmentManager> TYPE = new Type<>(EnchantmentManager::new, EnchantmentManager::createFromNbt, null);
    private static final int BASE = Registries.ENCHANTMENT.size();
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

    public static long writeEnchantments(Set<Enchantment> enchantmentsSet) {
        if (enchantmentsSet.isEmpty())
            return -1;

        long encodedEnchantments = 0;
        int i = 0;
        for (Enchantment enchantment : enchantmentsSet) {
            encodedEnchantments += (long) (Registries.ENCHANTMENT.getRawId(enchantment) * Math.pow(BASE, i));
            ++i;
        }

        return encodedEnchantments;
    }

    public static Set<Enchantment> readEnchantments(long enchantments) {
        Set<Enchantment> enchantmentsSet = new HashSet<>();
        while (enchantments > 0) {
            Enchantment enchantment = Registries.ENCHANTMENT.get((int) (enchantments % BASE));
            if (enchantment != null) {
                enchantmentsSet.add(enchantment);
                enchantments /= BASE;
            }
        }
        return enchantmentsSet;
    }
}
