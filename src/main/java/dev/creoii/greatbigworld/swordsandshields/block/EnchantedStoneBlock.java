package dev.creoii.greatbigworld.swordsandshields.block;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsBlockEntities;
import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentPlayer;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EnchantedStoneBlock extends BlockWithEntity {
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
        return new EnchantedStoneBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, SwordsAndShieldsBlockEntities.ENCHANTED_STONE, EnchantedStoneBlockEntity::tick);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (player instanceof EnchantmentPlayer enchantmentPlayer) {
            if (!world.isClient) {
                Optional<RegistryEntry.Reference<Enchantment>> enchantment = Registries.ENCHANTMENT.getRandom(player.getRandom());
                if (enchantment.isEmpty())
                    return ActionResult.PASS;
                enchantmentPlayer.gbw$addEnchantment(enchantment.get().value());
            }
            world.playSound(player, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1f, 1f);
        }
        return ActionResult.success(world.isClient);
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
