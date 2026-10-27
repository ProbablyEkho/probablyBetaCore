package dev.probablyekho.pbcore.item;

import dev.probablyekho.pbcore.sound.SoundRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CoinItem extends Item {
    public CoinItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            player.giveExperienceLevels(100);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegistry.CASH.get(), SoundSource.PLAYERS, 1.0F, (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.35F + 0.9F);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        itemstack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
