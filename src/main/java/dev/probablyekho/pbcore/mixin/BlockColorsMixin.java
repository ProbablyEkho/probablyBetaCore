package dev.probablyekho.pbcore.mixin;

import dev.probablyekho.pbcore.block.BlockRegistry;
import mod.bluestaggo.modernerbeta.client.color.block.BlockColorSampler;
import mod.bluestaggo.modernerbeta.client.color.block.BlockColors;
import mod.bluestaggo.modernerbeta.client.color.block.GrassTintSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockColors.class)
public class BlockColorsMixin {
    @Inject(method = "register", at = @At("TAIL"))
    private static void registerMoreStuff(BlockColors.BlockColorRegisterer registerer, CallbackInfo ci) {
        registerer.register(new GrassTintSource(BlockColorSampler.INSTANCE, false), BlockRegistry.BUSH.get());
    }
}
