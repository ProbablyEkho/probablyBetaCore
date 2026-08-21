package dev.probablyekho.pbcore.client;

import dev.probablyekho.pbcore.block.BlockRegistry;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@OnlyIn(Dist.CLIENT)
public class Tinting {
    public static void blockTint(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tint) -> level != null && pos != null
                    ? BiomeColors.getAverageGrassColor(level, pos)
                    : GrassColor.getDefaultColor(),
                BlockRegistry.BUSH.get()
        );
    }

    public static void itemTint(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tint) -> {
            BlockState blockstate = ((BlockItem)stack.getItem()).getBlock().defaultBlockState();
            return event.getBlockColors().getColor(blockstate, null, null, tint);
        },
        BlockRegistry.BUSH.get());
    }
}
