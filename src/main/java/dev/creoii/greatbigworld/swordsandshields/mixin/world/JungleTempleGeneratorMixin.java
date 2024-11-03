package dev.creoii.greatbigworld.swordsandshields.mixin.world;

import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentPlayer;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.JungleTempleGenerator;
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

@Mixin(JungleTempleGenerator.class)
public abstract class JungleTempleGeneratorMixin extends ShiftableStructurePiece {
    protected JungleTempleGeneratorMixin(StructurePieceType type, int x, int y, int z, int width, int height, int depth, Direction orientation) {
        super(type, x, y, z, width, height, depth, orientation);
    }

    @Inject(method = "generate", at = @At(value = "FIELD", target = "Lnet/minecraft/structure/JungleTempleGenerator;placedHiddenChest:Z", ordinal = 0))
    private void gbw$addJungleTempleEnchantedStone(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot, CallbackInfo ci) {
        EnchantmentPlayer.addEnchantedStone(world, offsetPos(6, 1, 10), Enchantments.FEATHER_FALLING, chunkBox);
        EnchantmentPlayer.addEnchantedStone(world, offsetPos(5, 1, 10), Enchantments.SILK_TOUCH, chunkBox);
    }
}
