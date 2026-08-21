package dev.probablyekho.pbcore.item;

import dev.probablyekho.pbcore.entity.ProjectileFireball;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;

public class FireballItem extends Item implements ProjectileItem {
    public FireballItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            LargeFireball largeFireball = new LargeFireball(level, player, player.getLookAngle(), 1);
            largeFireball.setPos(player.getX(), player.getY() + 0.25, player.getZ());
            largeFireball.setDeltaMovement(player.getLookAngle());
            level.addFreshEntity(largeFireball);
        }

        level.playSound(
            null,
            player.getX(),
            player.getY(),
            player.getZ(),
            SoundEvents.GHAST_SHOOT,
            SoundSource.NEUTRAL,
            0.5F,
            0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        ItemStack itemstack = player.getItemInHand(hand);
        player.getCooldowns().addCooldown(this, 20);
        player.awardStat(Stats.ITEM_USED.get(this));
        itemstack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack itemStack, Direction direction) {
        double d0 = direction.getStepX();
        double d1 = direction.getStepY();
        double d2 = direction.getStepZ();
        Vec3 vec3 = new Vec3(d0, d1, d2);
        ProjectileFireball projectileFireball = new ProjectileFireball(level, vec3);
        projectileFireball.setPos(pos.x(), pos.y(), pos.z());
        projectileFireball.setItem(itemStack);
        return projectileFireball;
    }

    @Override
    public ProjectileItem.DispenseConfig createDispenseConfig() {
        return ProjectileItem.DispenseConfig.builder()
            .positionFunction((blockSource, direction) -> DispenserBlock.getDispensePosition(blockSource, 1.0, Vec3.ZERO))
            .uncertainty(0.0F)
            .power(1.0F)
            .overrideDispenseEvent(1018)
            .build();
    }
}
