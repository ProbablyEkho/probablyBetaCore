package dev.probablyekho.pbcore;

import dev.probablyekho.pbcore.client.RedRenderer;
import dev.probablyekho.pbcore.client.Tinting;
import dev.probablyekho.pbcore.entity.EntityRegistry;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = probablyBetaCore.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = probablyBetaCore.MODID, value = Dist.CLIENT)
public class probablyBetaCoreClient {
    public probablyBetaCoreClient(IEventBus modEventBus, ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        modEventBus.addListener(Tinting::blockTint);
        modEventBus.addListener(Tinting::itemTint);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        probablyBetaCore.LOGGER.info("HELLO FROM CLIENT SETUP");
        probablyBetaCore.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    static void rendererRegistry(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
            EntityRegistry.PRIMED_RED.get(),
            RedRenderer::new
        );
    }
}
