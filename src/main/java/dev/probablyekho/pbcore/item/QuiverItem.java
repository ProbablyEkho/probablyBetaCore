package dev.probablyekho.pbcore.item;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class QuiverItem extends ArmorItem {
    public QuiverItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    public static boolean hasArrows(ItemStack itemStack) {
        return itemStack.getDamageValue() < itemStack.getMaxDamage();
    }

    public static void consumeArrow(ItemStack itemStack) {
        itemStack.setDamageValue(itemStack.getDamageValue() + 1);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack itemStack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if(action == ClickAction.SECONDARY && slot.allowModification(player) && other.is(Items.ARROW)) {
            int damageValue = itemStack.getDamageValue();
            if (damageValue == 0) return true;
            int inserted = Math.min(other.getCount(), damageValue);
            itemStack.setDamageValue(damageValue - inserted);
            other.shrink(inserted);
            player.playSound(SoundEvents.ARMOR_EQUIP_LEATHER.value(), 0.8F, 1.0F);
            return true;
        } else return false;
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return ItemAttributeModifiers.EMPTY;
    }
}
