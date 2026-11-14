package dev.probablyekho.pbcore.mixin;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal = 0))
    private boolean removeUsingItemPenalty(LocalPlayer instance) {
        return false;
    }
    @Inject(method = "aiStep", at = @At("TAIL"))
    private void noSprintInFluids(CallbackInfo ci) {
        LocalPlayer localPlayer = (LocalPlayer)(Object)this;
        if(localPlayer.isInLiquid()) {
            localPlayer.setSprinting(false);
        }
    }
}
