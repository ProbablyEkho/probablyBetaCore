package dev.probablyekho.pbcore.mixin;

import mett.palemannie.squakeported.ISquakeEntity;
import mett.palemannie.squakeported.SquakeClientPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SquakeClientPlayer.class)
public class SquakeClientPlayerMixin {
    @Inject(method = "moveEntityWithHeading", at = @At("HEAD"), cancellable = true)
    private static void sprintOnlySquake(Player player, ISquakeEntity squakeEntity, float sidemove, float upmove, float forwardmove, CallbackInfoReturnable<Boolean> cir) {
        if(!player.isSprinting()) {
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "moveRelative", at = @At("HEAD"), cancellable = true)
    private static void fixNonSprintMovement(Player player, ISquakeEntity squakeEntity, float sidemove, float upmove, float forwardmove, float friction, CallbackInfoReturnable<Boolean> cir) {
        if(!player.isSprinting()) {
            cir.setReturnValue(false);
        }
    }
}
