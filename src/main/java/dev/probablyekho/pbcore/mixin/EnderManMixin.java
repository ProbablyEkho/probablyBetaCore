package dev.probablyekho.pbcore.mixin;

import dev.probablyekho.pbcore.sound.BansheeCrySoundHandler;
import dev.probablyekho.pbcore.sound.SoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(EnderMan.class)
public abstract class EnderManMixin extends Monster implements NeutralMob {
    @Shadow protected abstract boolean teleport();

    @Shadow public abstract boolean isCreepy();

    @Unique @Nullable private BansheeCrySoundHandler crySoundHandler;

    protected EnderManMixin(EntityType<? extends EnderMan> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @author ProbablyEkho
     * @reason Expands the enderman stare aggro area
     */
    @Overwrite
    public boolean isLookingAtMe(Player player) {
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

    /**
     * @author ProbablyEkho
     * @reason Makes them teleport after beign hurt
     */
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

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void tears(CallbackInfo ci) {
        if (this.level().isClientSide) {
            SoundManager soundManager = Minecraft.getInstance().getSoundManager();
            if(this.isCreepy()) {
                if(this.random.nextInt(7) == 0) {
                    this.level().addParticle(ParticleTypes.SNOWFLAKE, this.getX(), this.getY() + 2.5, this.getZ(), Mth.randomBetween(this.random, -1.0F, 1.0F) * 0.083333336F, 0.05F, Mth.randomBetween(this.random, -1.0F, 1.0F) * 0.083333336F);
                }
                if(crySoundHandler == null || crySoundHandler.isStopped()) {
                    crySoundHandler = new BansheeCrySoundHandler((EnderMan)(Object)this, SoundRegistry.BANSHEE_CRY.get());
                    soundManager.play(crySoundHandler);
                } else {
                    crySoundHandler.fadeIn();
                }
            } else if(crySoundHandler != null && !crySoundHandler.isStopped()) {
                crySoundHandler.fadeOut();
            }
        }
    }

    @Redirect(method = "setTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;addTransientModifier(Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)V"))
    private void noSpeedUp(AttributeInstance attribute, AttributeModifier modifier) {
        if(attribute.getAttribute() != Attributes.MOVEMENT_SPEED) {
            attribute.addTransientModifier(modifier);
        }
    }
}
