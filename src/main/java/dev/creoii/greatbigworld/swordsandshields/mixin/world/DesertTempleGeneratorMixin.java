package dev.creoii.greatbigworld.swordsandshields.mixin.world;

import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentPlayer;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.DesertTempleGenerator;
import net.minecraft.structure.ShiftableStructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DesertTempleGenerator.class)
public abstract class DesertTempleGeneratorMixin extends ShiftableStructurePiece {
    protected DesertTempleGeneratorMixin(StructurePieceType type, int x, int y, int z, int width, int height, int depth, Direction orientation) {
        super(type, x, y, z, width, height, depth, orientation);
    }

    @Inject(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/structure/DesertTempleGenerator;generateBasement(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/util/math/BlockBox;)V"))
    private void gbw$addDesertTempleEnchantedStone(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot, CallbackInfo ci) {
        EnchantmentPlayer.addEnchantedStone(world, offsetPos(8, -8, 10), Enchantments.FIRE_PROTECTION, chunkBox);
        EnchantmentPlayer.addEnchantedStone(world, offsetPos(12, -8, 10), Enchantments.FIRE_PROTECTION, chunkBox);
        EnchantmentPlayer.addEnchantedStone(world, offsetPos(10, -8, 8), Enchantments.FIRE_PROTECTION, chunkBox);
        EnchantmentPlayer.addEnchantedStone(world, offsetPos(10, -8, 12), Enchantments.FIRE_PROTECTION, chunkBox);
    }
}
