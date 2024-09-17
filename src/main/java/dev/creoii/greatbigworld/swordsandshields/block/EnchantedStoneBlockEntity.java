package dev.creoii.greatbigworld.swordsandshields.block;

import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldBlocks;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsBlockEntities;
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
import net.minecraft.world.World;

public class EnchantedStoneBlockEntity extends BlockEntity {
    private Enchantment enchantment = null;
    private boolean nearPlayers = false;

    public EnchantedStoneBlockEntity(BlockPos pos, BlockState state) {
        super(SwordsAndShieldsBlockEntities.ENCHANTED_STONE, pos, state);
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public void setEnchantment(Enchantment enchantment) {
        this.enchantment = enchantment;
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

    public static void tick(World world, BlockPos pos, BlockState state, EnchantedStoneBlockEntity blockEntity) {
        PlayerEntity playerEntity = world.getClosestPlayer(pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d, 9d, false);
        if (playerEntity != null && state.isOf(SwordsAndShieldBlocks.ENCHANTED_STONE)) {
            if (!world.isClient) {
                blockEntity.nearPlayers = true;

                double distance = pos.toCenterPos().distanceTo(playerEntity.getPos());
                int glow = state.get(EnchantedStoneBlock.GLOW);
                if (distance < 4 && distance >= 0 && glow != 3) {
                    world.setBlockState(pos, state.with(EnchantedStoneBlock.GLOW, 3));
                } else if (distance < 7 && distance >= 4 && glow != 2) {
                    world.setBlockState(pos, state.with(EnchantedStoneBlock.GLOW, 2));
                } else if (distance >= 7 && glow != 1) {
                    world.setBlockState(pos, state.with(EnchantedStoneBlock.GLOW, 1));
                }
            }
        } else if (blockEntity.nearPlayers && !world.isClient) {
            blockEntity.nearPlayers = false;
            world.setBlockState(pos, state.with(EnchantedStoneBlock.GLOW, 0));
        }

        if (world.getTime() % 4 == 0 && world.getRandom().nextBoolean() && blockEntity.nearPlayers) {
            world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 1f, 1f);
        }
    }
}
