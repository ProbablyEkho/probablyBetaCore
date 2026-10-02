package dev.probablyekho.pbcore.mixin;

import net.atired.creaturefeature.entity.CoatOfArmsEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(CoatOfArmsEntity.class)
public class CoatOfArmsEntityMixin {
    @Unique CoatOfArmsEntity coatOfArms = (CoatOfArmsEntity)(Object)this;
    @ModifyArgs(method = "tick", at = @At(value = "INVOKE", target = "Lnet/atired/creaturefeature/entity/BulletEntity;setPos(Lnet/minecraft/world/phys/Vec3;)V"))
    private void centeredBullets(Args args) {
        args.set(0, coatOfArms.getPosition(1.0F).add((Math.random() - (double)0.5F) / (double)4.0F, 1.0F + ((Math.random() - (double)0.5F) / (double)4.0F), (Math.random() - (double)0.5F) / (double)4.0F));
    }
    /*
    @Inject(method = "tick", at = @At("TAIL"))
    private void backAwayFromTarget(CallbackInfo ci) {
        if(!coatOfArms.level().isClientSide() && coatOfArms.getTarget() != null) {
            float distanceToTarget = coatOfArms.distanceTo(coatOfArms.getTarget());
            if(distanceToTarget < 7.0F) {
                if(coatOfArms.delayOfShooting <= 0) {
                    Vec3 backAway = coatOfArms.position().subtract(coatOfArms.getTarget().position()).normalize();
                    coatOfArms.setDeltaMovement(backAway.x * 0.2F, coatOfArms.getDeltaMovement().y, backAway.z * 0.2F);
                }
            }
        }
    }
    */
}
