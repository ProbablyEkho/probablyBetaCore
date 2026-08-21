package dev.probablyekho.pbcore.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderMan.class)
public abstract class EnderManMixin extends Monster implements NeutralMob {
    protected EnderManMixin(EntityType<? extends EnderMan> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
    }

    @Inject(method = "isSensitiveToWater", at = @At("HEAD"), cancellable = true)
    private void notSensitiveToWater(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void hurtFromProjectiles(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.isInvulnerableTo(source)) cir.setReturnValue(false);
        else cir.setReturnValue(super.hurt(source, amount));
    }

    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void lowerHealth(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue().add(Attributes.MAX_HEALTH, 30.0);
    }
}
