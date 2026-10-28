package dev.probablyekho.pbcore.mixin;

import dev.probablyekho.pbcore.block.BlockRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BlockMarker;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockMarker.class)
public class BlockMarkerMixin {
    @Unique private boolean isAsof;
    @Inject(method = "<init>", at = @At("TAIL"))
    private void asofBlockCheck(ClientLevel level, double x, double y, double z, BlockState state, CallbackInfo ci) {
        this.isAsof = state.is(BlockRegistry.ASOF.get());
    }
    @Inject(method = "getQuadSize", at = @At("RETURN"), cancellable = true)
    private void pixelConsistentAsof(float scaleFactor, CallbackInfoReturnable<Float> cir) {
        if(this.isAsof) {
            cir.setReturnValue(cir.getReturnValue() * 4);
        }
    }
}
