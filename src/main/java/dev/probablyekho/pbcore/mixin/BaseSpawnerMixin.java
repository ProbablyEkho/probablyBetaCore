package dev.probablyekho.pbcore.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseSpawner.class)
public abstract class BaseSpawnerMixin {
    @Shadow private int spawnDelay;

    @Inject(method = "delay", at = @At("TAIL"))
    private void longerDelay(Level level, BlockPos pos, CallbackInfo ci) {
        this.spawnDelay = (int) (this.spawnDelay * 1.5);
    }
    @ModifyArg(method = "clientTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 1))
    private ParticleOptions soulFlames(ParticleOptions particleData) {
        return ParticleTypes.SOUL_FIRE_FLAME;
    }
}
