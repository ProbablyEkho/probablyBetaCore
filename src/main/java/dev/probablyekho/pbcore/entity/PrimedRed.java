package dev.probablyekho.pbcore.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PrimedRed extends PrimedTnt {
    public PrimedRed(EntityType<? extends PrimedTnt> entityType, Level level) {
        super(entityType, level);
    }

    public PrimedRed(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        this(EntityRegistry.PRIMED_RED.get(), level);
        this.setPos(x, y, z);
        double d0 = level.random.nextDouble() * (float) (Math.PI * 2);
        this.setDeltaMovement(-Math.sin(d0) * 0.02, 0.2F, -Math.cos(d0) * 0.02);
        this.setFuse(600);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    protected void explode() {
        this.level()
            .explode(
                this,
                Explosion.getDefaultDamageSource(this.level(), this),
                null,
                this.getX(),
                this.getY(0.0625),
                this.getZ(),
                40.0F,
                true,
                Level.ExplosionInteraction.TNT
            );
    }
}
