package dev.probablyekho.pbcore.mixin;

import dev.probablyekho.pbcore.block.BlockRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.PumpkinBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PumpkinBlock.class)
public class PumpkinBlockMixin {
    @Redirect(method = "useItemOn", at = @At(value = "FIELD", target = "Lnet/minecraft/world/item/Items;PUMPKIN_SEEDS:Lnet/minecraft/world/item/Item;"))
    private Item gourdSeedsFromCarving() {
        return BlockRegistry.GOURD_CROP.asItem();
    }
}
