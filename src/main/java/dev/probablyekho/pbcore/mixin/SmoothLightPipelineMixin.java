package dev.probablyekho.pbcore.mixin;

import net.caffeinemc.mods.sodium.client.model.light.smooth.SmoothLightPipeline;
import net.caffeinemc.mods.sodium.client.model.quad.ModelQuadView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SmoothLightPipeline.class)
public class SmoothLightPipelineMixin {
    @Redirect(method = "calculate", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/model/quad/ModelQuadView;getFlags()I"))
    private int noPartialFaceShading(ModelQuadView modelQuadView) {
        int flags = modelQuadView.getFlags();
        if((flags & 1) != 0) {
            flags -= 1;
        }
        return flags;
    }
}
