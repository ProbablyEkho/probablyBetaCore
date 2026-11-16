package dev.probablyekho.pbcore.mixin;

import dev.probablyekho.pbcore.block.BlockRegistry;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IItemExtension.class)
public interface IItemExtensionMixin {
    @Inject(method = "isEnderMask", at = @At("HEAD"), cancellable = true)
    private void enderMasks(ItemStack stack, Player player, EnderMan endermanEntity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(stack.getItem() == Blocks.CARVED_PUMPKIN.asItem() || stack.getItem() == BlockRegistry.CARVED_GHOST_PUMPKIN.asItem());
    }
}
