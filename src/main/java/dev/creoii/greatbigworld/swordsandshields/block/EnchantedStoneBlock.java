package dev.creoii.greatbigworld.swordsandshields.block;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsBlockEntities;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsCriteria;
import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentPlayer;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EnchantedStoneBlock extends BlockWithEntity {
    private static final Random RANDOM = Random.create();
    public static final IntProperty GLOW = IntProperty.of("glow", 0, 3);

    public EnchantedStoneBlock() {
        super(Settings.copy(Blocks.STONE).luminance(state -> state.get(GLOW) * 3));
        setDefaultState(getStateManager().getDefaultState().with(GLOW, 0));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        EnchantedStoneBlockEntity blockEntity = new EnchantedStoneBlockEntity(pos, state);
        Optional<RegistryEntry.Reference<Enchantment>> enchantment = Registries.ENCHANTMENT.getRandom(RANDOM);
        if (enchantment.isEmpty())
            return null;
        blockEntity.setEnchantment(enchantment.get().value());
        return blockEntity;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, SwordsAndShieldsBlockEntities.ENCHANTED_STONE, EnchantedStoneBlockEntity::tick);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (player instanceof EnchantmentPlayer enchantmentPlayer) {
            if (!world.isClient) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof EnchantedStoneBlockEntity enchantedStoneBlockEntity) {
                    if (enchantmentPlayer.gbw$addEnchantment(enchantedStoneBlockEntity.getEnchantment())) {
                        SwordsAndShieldsCriteria.ENCHANTMENT_LEARNED.trigger((ServerPlayerEntity) player, Registries.ENCHANTMENT.getId(enchantedStoneBlockEntity.getEnchantment()));
                    } else return ActionResult.PASS;
                }
            }
            world.playSound(player, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1f, 1f);
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        for (int i = 0; i < 4; ++i) {
            double d = (double) pos.getX() + random.nextDouble();
            double f = (double) pos.getZ() + random.nextDouble();
            double g = ((double) random.nextFloat() - .5d) * .5d;
            double j = ((double) random.nextFloat() - .5d) * .5d;
            int k = random.nextInt(2) * 2 - 1;
            if (!world.getBlockState(pos.west()).isOf(this) && !world.getBlockState(pos.east()).isOf(this)) {
                d = (double) pos.getX() + .5d + .25d * (double) k;
                g = random.nextFloat() * 2f * (float) k;
            } else {
                f = (double) pos.getZ() + .5d + .25d * (double) k;
                j = random.nextFloat() * 2f * (float) k;
            }

            world.addParticle(ParticleTypes.ENCHANT, d, (double) pos.getY() + random.nextDouble(), f, g, ((double) random.nextFloat() - .5d) * .5d, j);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(GLOW);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
