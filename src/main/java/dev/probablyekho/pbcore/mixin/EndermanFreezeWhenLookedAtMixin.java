package dev.probablyekho.pbcore.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.entity.monster.EnderMan$EndermanFreezeWhenLookedAt")
public class EndermanFreezeWhenLookedAtMixin {
    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void noFreeze(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
