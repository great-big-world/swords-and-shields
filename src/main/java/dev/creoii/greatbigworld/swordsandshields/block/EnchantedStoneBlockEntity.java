package dev.creoii.greatbigworld.swordsandshields.block;

import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsBlockEntities;
import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentPlayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class EnchantedStoneBlockEntity extends BlockEntity {
    @Nullable
    private Enchantment enchantment = null;
    private boolean nearPlayers = false;
    @Nullable
    private PlayerEntity cachedPlayer = null;

    public EnchantedStoneBlockEntity(BlockPos pos, BlockState state) {
        super(SwordsAndShieldsBlockEntities.ENCHANTED_STONE, pos, state);
    }

    public @Nullable Enchantment getEnchantment() {
        return enchantment;
    }

    public @Nullable PlayerEntity getCachedPlayer() {
        return cachedPlayer;
    }

    public void setEnchantment(@Nullable Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    public void setCachedPlayer(@Nullable PlayerEntity cachedPlayer) {
        this.cachedPlayer = cachedPlayer;
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nearPlayers = nbt.getBoolean("near_players");
        enchantment = Registries.ENCHANTMENT.get(nbt.getInt("enchantment"));
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putBoolean("near_players", nearPlayers);
        nbt.putInt("enchantment", Registries.ENCHANTMENT.getRawId(enchantment));
    }

    public static void tickServer(World world, BlockPos pos, BlockState state, EnchantedStoneBlockEntity blockEntity) {
        if (blockEntity.getCachedPlayer() == null || (blockEntity.getCachedPlayer() instanceof EnchantmentPlayer enchantmentPlayer && enchantmentPlayer.gbw$getEnchantments().contains(blockEntity.getEnchantment())))
            blockEntity.cachedPlayer = findClosestValidPlayer(world, pos, blockEntity);

        if (blockEntity.getCachedPlayer() != null) {
            blockEntity.nearPlayers = true;

            double distance = pos.toCenterPos().squaredDistanceTo(blockEntity.getCachedPlayer().getPos());
            if (distance <= 81) {
                int glow = state.get(EnchantedStoneBlock.GLOW);
                if (distance < 16 && distance >= 0 && glow != 3) {
                    world.setBlockState(pos, state.with(EnchantedStoneBlock.GLOW, 3));
                } else if (distance < 49 && distance >= 16 && glow != 2) {
                    world.setBlockState(pos, state.with(EnchantedStoneBlock.GLOW, 2));
                } else if (distance >= 49 && glow != 1) {
                    world.setBlockState(pos, state.with(EnchantedStoneBlock.GLOW, 1));
                }
            } else {
                blockEntity.cachedPlayer = null;
                blockEntity.nearPlayers = false;
                world.setBlockState(pos, state.with(EnchantedStoneBlock.GLOW, 0));
            }
        } else if (blockEntity.nearPlayers) {
            blockEntity.nearPlayers = false;
            blockEntity.cachedPlayer = null;
            world.setBlockState(pos, state.with(EnchantedStoneBlock.GLOW, 0));
        }

        if (blockEntity.nearPlayers && world.getTime() % 4 == 0 && world.getRandom().nextBoolean()) {
            world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 1f, 1f);
        }
    }

    @Nullable
    private static PlayerEntity findClosestValidPlayer(World world, BlockPos pos, EnchantedStoneBlockEntity blockEntity) {
        List<PlayerEntity> players = world.getEntitiesByClass(PlayerEntity.class, new Box(pos).expand(9d), player -> !player.isSpectator() && player instanceof EnchantmentPlayer enchantmentPlayer && !enchantmentPlayer.gbw$getEnchantments().contains(blockEntity.getEnchantment()));
        if (players.isEmpty())
            return null;
        else {
            players.sort(Comparator.comparingDouble(player -> pos.toCenterPos().squaredDistanceTo(player.getPos())));
            return players.getFirst();
        }
    }
}
