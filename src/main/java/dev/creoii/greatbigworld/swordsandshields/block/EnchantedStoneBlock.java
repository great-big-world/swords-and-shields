package dev.creoii.greatbigworld.swordsandshields.block;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsGameEvents;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsBlockEntities;
import dev.creoii.greatbigworld.swordsandshields.registry.SwordsAndShieldsCriteria;
import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentPlayer;
import dev.creoii.greatbigworld.swordsandshields.util.LearnEnchantment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

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
        return world.isClient ? null : validateTicker(type, SwordsAndShieldsBlockEntities.ENCHANTED_STONE, EnchantedStoneBlockEntity::tickServer);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (player instanceof EnchantmentPlayer enchantmentPlayer /*&& state.get(GLOW) != 0*/) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (!world.isClient && blockEntity instanceof EnchantedStoneBlockEntity enchantedStoneBlockEntity) {
                enchantedStoneBlockEntity.refreshCachedPlayer(world, pos);
                Identifier enchantmentId = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getRandom(world.getRandom()).get().getKey().get().getValue();
                SwordsAndShieldsCriteria.ENCHANTMENT_LEARNED.trigger((ServerPlayerEntity) player, enchantmentId);
                ServerPlayNetworking.send((ServerPlayerEntity) player, new LearnEnchantment(enchantmentId));
            }

            if (blockEntity instanceof EnchantedStoneBlockEntity enchantedStoneBlockEntity && enchantedStoneBlockEntity.hasEnchantment()) {
                if (enchantmentPlayer.gbw$addEnchantment(enchantedStoneBlockEntity.getEnchantment())) {
                    System.out.println("server enchantplayer count: " + enchantmentPlayer.gbw$getEnchantments().size());
                    if (!world.isClient) {
                        enchantedStoneBlockEntity.refreshCachedPlayer(world, pos);
                        SwordsAndShieldsCriteria.ENCHANTMENT_LEARNED.trigger((ServerPlayerEntity) player, enchantedStoneBlockEntity.getEnchantment().getValue());
                        ServerPlayNetworking.send((ServerPlayerEntity) player, new LearnEnchantment(enchantedStoneBlockEntity.getEnchantment().getValue()));
                    }
                    world.emitGameEvent(SwordsAndShieldsGameEvents.LEARN_ENCHANTMENT, pos, GameEvent.Emitter.of(player, state));
                    world.playSound(player, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1f, 1f);
                    return ActionResult.success(world.isClient);
                }
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (state.get(GLOW) != 0 && blockEntity instanceof EnchantedStoneBlockEntity enchantedStoneBlockEntity && enchantedStoneBlockEntity.hasEnchantment()) {
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
