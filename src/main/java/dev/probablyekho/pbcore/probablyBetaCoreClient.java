package dev.probablyekho.pbcore;

import dev.probablyekho.pbcore.block.BlockRegistry;
import dev.probablyekho.pbcore.client.*;
import dev.probablyekho.pbcore.entity.EntityRegistry;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.shader.program.ShaderProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = probablyBetaCore.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = probablyBetaCore.MODID, value = Dist.CLIENT)
public class probablyBetaCoreClient {
    private static final ResourceLocation SORETRO_SHADER = ResourceLocation.fromNamespaceAndPath(probablyBetaCore.MODID, "soretro");
    private static boolean shaderApplied = false;
    public probablyBetaCoreClient(IEventBus modEventBus, ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        modEventBus.addListener(Tinting::blockTint);
        modEventBus.addListener(Tinting::itemTint);
    }

    @SubscribeEvent
    static void rendererRegistry(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
            EntityRegistry.PRIMED_RED.get(),
            RedRenderer::new
        );
        event.registerEntityRenderer(
            EntityRegistry.JELLYFISH.get(),
            JellyfishRenderer::new
        );
        event.registerEntityRenderer(
            EntityRegistry.MONEY.get(),
            ThrownItemRenderer::new
        );
    }
    @SubscribeEvent
    public static void layerRegistry(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(probablyBetaCore.JELLYFISH_LAYER, JellyfishModel::createBodyLayer);
    }
    @SubscribeEvent
    public static void ghostPumpkinBlur(RegisterClientExtensionsEvent event) {
        event.registerItem(new GhostPumpkinOverlay(), BlockRegistry.CARVED_GHOST_PUMPKIN.get().asItem());
    }
    @SubscribeEvent
    public static void soRetroPostShader(ClientTickEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        if(player != null) {
            ShaderProgram shaderProgram = VeilRenderSystem.renderer().getShaderManager().getShader(SORETRO_SHADER);
            if(!shaderApplied) {
                VeilRenderSystem.renderer().getPostProcessingManager().add(SORETRO_SHADER);
                shaderApplied = true;
            }
            if(shaderProgram != null && shaderProgram.isValid()) {
                shaderProgram.getUniform("Resolution").setFloat(96F);
                shaderProgram.getUniform("MosaicSize").setFloat(2F);
                shaderProgram.getUniform("PlayerHealth").setFloat(player.isCreative() || player.isSpectator() || player.getHealth() > player.getMaxHealth() * 0.5F ? 0F : Math.clamp(1.0F - player.getHealth() / (player.getMaxHealth() * 0.5F), 0F, 1F));
                shaderProgram.getUniform("Dead").setFloat(player.getHealth() > 0F ? 0F : 1F);
            }
        }
    }
}
