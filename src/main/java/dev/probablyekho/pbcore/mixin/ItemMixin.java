package dev.probablyekho.pbcore.mixin;

import dev.probablyekho.pbcore.item.ItemRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true)
    private void lapisRepair(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access, CallbackInfoReturnable<Boolean> cir) {
        if(action != ClickAction.SECONDARY || !other.is(Items.LAPIS_LAZULI) || stack.is(ItemRegistry.QUIVER)) return;
        if(!stack.isDamageableItem() || !stack.isDamaged()) {
            cir.setReturnValue(true);
            return;
        }
        stack.setDamageValue(Math.max(0, stack.getDamageValue() - 64));
        other.shrink(1);
        player.playSound(SoundEvents.ITEM_PICKUP);
        cir.setReturnValue(true);
    }
}
