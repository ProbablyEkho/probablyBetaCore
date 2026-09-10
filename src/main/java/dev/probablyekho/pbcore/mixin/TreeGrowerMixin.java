package dev.probablyekho.pbcore.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TreeGrower.class)
public class TreeGrowerMixin {
    @Inject(method = "getConfiguredMegaFeature", at = @At("HEAD"), cancellable = true)
    private void stopMegaTreeGrowth(RandomSource random, CallbackInfoReturnable<ResourceKey<ConfiguredFeature<?, ?>>> cir) {
        cir.setReturnValue(null);
    }
}
