package dev.probablyekho.pbcore.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {
    @Shadow
    protected EditBox input;
    @Unique
    ChatScreen screen = (ChatScreen)(Object)this;

    @Inject(method = "init", at = @At("TAIL"))
    private void modifyTextWidth(CallbackInfo ci) {
        this.input.setX(12);
        this.input.setWidth((int) ((screen.width * 0.75F) - 24));
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void chatInputArrow(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        guiGraphics.drawString(screen.getMinecraft().font, ">", 4, screen.height - 12, 16777215);
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"))
    private void halveBoxWidth(Args args) {
        args.set(0, 0);
        args.set(1, screen.height - 16);
        args.set(2, (int) (screen.width * 0.75F));
        args.set(3, screen.height);
    }
}