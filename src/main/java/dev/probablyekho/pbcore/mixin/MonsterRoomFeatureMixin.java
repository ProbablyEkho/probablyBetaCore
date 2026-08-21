package dev.probablyekho.pbcore.mixin;

import dev.probablyekho.pbcore.Config;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MonsterRoomFeature.class)
public class MonsterRoomFeatureMixin {
    @ModifyArg(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/MonsterRoomFeature;safeSetBlock(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Ljava/util/function/Predicate;)V", ordinal = 0), index = 2)
    private BlockState replaceCobble(BlockState blockState) {
        return Config.getDungeonCobblestone();
    }
    @ModifyArg(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/MonsterRoomFeature;safeSetBlock(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Ljava/util/function/Predicate;)V", ordinal = 1), index = 2)
    private BlockState replaceMossyCobble(BlockState blockState) {
        return Config.getDungeonMossyCobblestone();
    }
}
