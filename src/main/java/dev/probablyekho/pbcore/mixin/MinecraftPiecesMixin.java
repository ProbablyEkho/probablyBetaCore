package dev.probablyekho.pbcore.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MineshaftPieces.MineShaftCorridor.class)
public class MinecraftPiecesMixin {
    @ModifyArg(method = "fillPillarDownOrChainUp", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftCorridor;fillColumnBetween(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos$MutableBlockPos;II)V"), index = 1)
    private BlockState chainsToLogs(BlockState blockState) {
        return Blocks.ACACIA_LOG.defaultBlockState();
    }

    @ModifyArg(method = "fillPillarDownOrChainUp", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/WorldGenLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"), index = 1)
    private BlockState petrifiedFenceButLogActually(BlockState blockState) {
        return Blocks.ACACIA_LOG.defaultBlockState();
    }

    @ModifyArg(method = "placeSupport", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftCorridor;generateBox(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;IIIIIILnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Z)V", ordinal = 0), index = 8)
    private BlockState fixLeftSupportCrash(BlockState blockState) {
        return Blocks.ACACIA_LOG.defaultBlockState();
    }

    @ModifyArg(method = "placeSupport", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftCorridor;generateBox(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;IIIIIILnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Z)V", ordinal = 1), index = 8)
    private BlockState fixRightSupportCrash(BlockState blockState) {
        return Blocks.ACACIA_LOG.defaultBlockState();
    }
}
