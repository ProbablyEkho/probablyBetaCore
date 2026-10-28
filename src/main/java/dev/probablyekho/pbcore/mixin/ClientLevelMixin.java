package dev.probablyekho.pbcore.mixin;

import dev.probablyekho.pbcore.block.BlockRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Inject(method = "getMarkerParticleTarget()Lnet/minecraft/world/level/block/Block;", at = @At(value = "RETURN"), cancellable = true)
    private void advancedAsofDisplayTechnology(CallbackInfoReturnable<Block> cir) {
        if(cir.getReturnValue() == null) {
            cir.setReturnValue(BlockRegistry.ASOF.get());
        }
    }
}
