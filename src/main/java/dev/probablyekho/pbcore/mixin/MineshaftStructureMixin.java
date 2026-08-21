package dev.probablyekho.pbcore.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MineshaftStructure.Type.class)
public class MineshaftStructureMixin {
    @Inject(method = "getWoodState", at = @At("HEAD"), cancellable = true)
    private void petrifiedWood(CallbackInfoReturnable<BlockState> cir) {
        cir.setReturnValue(Blocks.ACACIA_LOG.defaultBlockState());
    }

    @Inject(method = "getPlanksState", at = @At("HEAD"), cancellable = true)
    private void petrifiedPlanks(CallbackInfoReturnable<BlockState> cir) {
        cir.setReturnValue(Blocks.ACACIA_PLANKS.defaultBlockState());
    }
}
