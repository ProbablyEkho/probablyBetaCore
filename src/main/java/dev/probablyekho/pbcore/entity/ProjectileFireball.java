package dev.probablyekho.pbcore.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ProjectileFireball extends LargeFireball {
    public ProjectileFireball(Level level, Vec3 movement) {
        super(EntityType.FIREBALL, level);
        this.setDeltaMovement(movement);
    }
}
