package dev.probablyekho.pbcore.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import dev.probablyekho.pbcore.probablyBetaCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;

@Mixin(MissingTextureAtlasSprite.class)
public class MissingTextureAtlasSpriteMixin {
    @Redirect(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/MissingTextureAtlasSprite;generateMissingImage(II)Lcom/mojang/blaze3d/platform/NativeImage;"))
    private static NativeImage customMissingTexture(int width, int height) {
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(probablyBetaCore.MODID, "textures/misc/missing.png");
        try {
            return NativeImage.read(Minecraft.getInstance().getResourceManager().getResourceOrThrow(texture).open());
        } catch (IOException e) {
            System.err.println("Failed to load the custom missing texture! Falling back to the vanilla magenta-black");
            NativeImage nativeimage = new NativeImage(width, height, false);
            for (int k = 0; k < height; k++) {
                for (int l = 0; l < width; l++) {
                    if (k < height / 2 ^ l < width / 2) {
                        nativeimage.setPixelRGBA(l, k, -524040);
                    } else {
                        nativeimage.setPixelRGBA(l, k, -16777216);
                    }
                }
            }
            return nativeimage;
        }
    }
}
