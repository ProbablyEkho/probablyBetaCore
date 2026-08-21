package dev.probablyekho.pbcore.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChatComponent.class)
public abstract class ChatComponentMixin {
    @Mutable
    @Final
    @Shadow
    private Minecraft minecraft;
    protected ChatComponentMixin(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    @ModifyReturnValue(method = "getWidth()I", at = @At("RETURN"))
    private int screenHalfWidth(int width) {
        return (minecraft.getWindow().getGuiScaledWidth() / 2) - 12;
    }
}