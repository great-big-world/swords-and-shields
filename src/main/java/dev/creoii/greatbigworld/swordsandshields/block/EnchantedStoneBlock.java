package dev.creoii.greatbigworld.swordsandshields.block;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsBlockEntities;
import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentPlayer;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
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

public class EnchantedStoneBlock extends BlockWithEntity {
    public static final IntProperty GLOW = IntProperty.of("glow", 0, 3);

    public EnchantedStoneBlock() {
        super(Settings.copy(Blocks.STONE).ticksRandomly().luminance(state -> state.get(GLOW) * 3));
        setDefaultState(getStateManager().getDefaultState().with(GLOW, 0));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EnchantedStoneBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, SwordsAndShieldsBlockEntities.ENCHANTED_STONE, EnchantedStoneBlockEntity::tick);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            if (player instanceof EnchantmentPlayer enchantmentPlayer) {
                enchantmentPlayer.gbw$addEnchantment(Enchantments.FIRE_ASPECT);
                world.playSound(player, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1f, world.random.nextFloat() * .1f + .9f);
            }
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 1f, .5f + world.random.nextFloat() * 1.2f);
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
        return state.get(GLOW) > 0;
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
