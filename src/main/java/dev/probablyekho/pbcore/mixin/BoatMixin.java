package dev.probablyekho.pbcore.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Boat.class)
public class BoatMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void cameraSteering(CallbackInfo ci) {
        Boat boat = (Boat)(Object)this;
        Entity passenger = boat.getControllingPassenger();
        if(passenger != null) {
            float lookOffset = ((passenger.getYRot() - boat.getYRot() + 180F) % 360F) - 180F;
            Vec3 velocity = boat.getDeltaMovement();
            boat.setYRot((boat.getYRot() + Math.max(-1.5F, Math.min(1.5F, lookOffset)) * (float) Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z)));
        }
    }
}