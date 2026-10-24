package dev.probablyekho.pbcore.mixin;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderMan.class)
public abstract class EnderManMixin extends Monster implements NeutralMob {
    @Shadow protected abstract boolean teleport();

    protected EnderManMixin(EntityType<? extends EnderMan> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @author ProbablyEkho
     * @reason Expands the enderman stare aggro area
     */
    @Overwrite
    boolean isLookingAtMe(Player player) {
        ItemStack itemstack = player.getInventory().armor.get(3);
        if (net.neoforged.neoforge.common.CommonHooks.shouldSuppressEnderManAnger((EnderMan)(Object)this, player, itemstack)) {
            return false;
        } else {
            Vec3 vec3 = player.getViewVector(1.0F).normalize();
            Vec3 vec31 = new Vec3(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
            vec31 = vec31.normalize();
            double d1 = vec3.dot(vec31);
            return d1 > 1.0 - 0.375 / vec31.length() && player.hasLineOfSight(this);
        }
    }

    @Overwrite
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            boolean hurted = super.hurt(source, amount);
            if(hurted && !this.level().isClientSide()) {
                for (int i = 0; i < 64; i++) {
                    if (this.teleport()) {
                        break;
                    }
                }
            }
            return hurted;
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean didHurtTarget = super.doHurtTarget(target);
        if(didHurtTarget && !this.level().isClientSide()) {
            for (int i = 0; i < 64; i++) {
                if (this.teleport()) {
                    break;
                }
            }
        }
        return didHurtTarget;
    }

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void replaceParticles(Level level, ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        ParticleOptions particle = this.random.nextInt(5) == 0 ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.SMOKE;
        level.addParticle(particle, x, y, z, 0,  0,  0);
    }

    @Redirect(method = "setTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;addTransientModifier(Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)V"))
    private void noSpeedUp(AttributeInstance attribute, AttributeModifier modifier) {
        if(attribute.getAttribute() != Attributes.MOVEMENT_SPEED) {
            attribute.addTransientModifier(modifier);
        }
    }
}
