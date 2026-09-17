package dev.probablyekho.pbcore.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.probablyekho.pbcore.probablyBetaCore;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class GhostPumpkinOverlay implements IClientItemExtensions {
    private static final ResourceLocation GHOST_PUMPKIN_BLUR_LOCATION = ResourceLocation.fromNamespaceAndPath(probablyBetaCore.MODID, "textures/misc/ghostpumpkinblur.png");
    @Override
    public void renderHelmetOverlay(ItemStack stack, Player player, GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        this.renderTextureOverlay(guiGraphics);
    }

    private void renderTextureOverlay(GuiGraphics guiGraphics) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, (float) 1.0);
        guiGraphics.blit(GhostPumpkinOverlay.GHOST_PUMPKIN_BLUR_LOCATION, 0, 0, -90, 0.0F, 0.0F, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
