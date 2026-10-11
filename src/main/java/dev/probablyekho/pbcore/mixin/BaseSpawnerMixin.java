package dev.probablyekho.pbcore.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseSpawner.class)
public class BaseSpawnerMixin {
    @Shadow private int spawnDelay;

    @Inject(method = "delay", at = @At("TAIL"))
    private void longerDelay(Level level, BlockPos pos, CallbackInfo ci) {
        this.spawnDelay = (int) (this.spawnDelay * 1.5);
    }
}
