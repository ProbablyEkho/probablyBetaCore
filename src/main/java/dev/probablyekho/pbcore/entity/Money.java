package dev.probablyekho.pbcore.entity;

import dev.probablyekho.pbcore.item.ItemRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class Money extends ThrowableItemProjectile {
    public Money(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public Money(Level level, LivingEntity shooter) {
        super(EntityRegistry.MONEY.get(), shooter, level);
    }

    public Money(Level level, double x, double y, double z) {
        super(EntityRegistry.MONEY.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.MONEY.asItem();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        if(entity instanceof Player player && !this.level().isClientSide) {
            player.giveExperienceLevels(100);
            this.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.35F + 0.9F);
        }
        if(!(entity instanceof Player)) {
            this.level().addFreshEntity(new ItemEntity(this.level(), result.getLocation().x, result.getLocation().y, result.getLocation().z, ItemRegistry.MONEY.toStack()));
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            if(!(result instanceof EntityHitResult)) {
                this.level().addFreshEntity(new ItemEntity(this.level(), result.getLocation().x, result.getLocation().y, result.getLocation().z, ItemRegistry.MONEY.toStack()));
            }
            this.discard();
        }
    }
}
