package dev.probablyekho.pbcore.mixin;

import net.minecraft.client.renderer.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderer.class)
public class GlowingNametagMixin {
    @ModifyVariable(method = "renderNameTag", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private int forceFullBright(int packedLight) {
        return 15728880;
    }
}
