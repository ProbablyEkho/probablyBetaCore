package dev.probablyekho.pbcore.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Function;

@Mixin(Creeper.class)
public abstract class CreeperMixin extends Monster implements PowerableMob {
    @Shadow
    protected abstract void spawnLingeringCloud();

    protected CreeperMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "explodeCreeper", at = @At("HEAD"), cancellable = true)
    private void explodeCreeperNerfed(CallbackInfo ci) {
        if (!this.level().isClientSide) {
            float f = this.isPowered() ? 2.0F : 1.0F;
            this.dead = true;
            this.level().explode(this, null, new SimpleExplosionDamageCalculator(true, true, Optional.of(2.5F * f), BuiltInRegistries.BLOCK.getTag(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())), this.getX(), this.getY(), this.getZ(), 3.0F + f, false, Level.ExplosionInteraction.TRIGGER);
            this.spawnLingeringCloud();
            this.triggerOnDeathMobEffects(Entity.RemovalReason.KILLED);
            this.discard();
        }

        ci.cancel();
    }
}