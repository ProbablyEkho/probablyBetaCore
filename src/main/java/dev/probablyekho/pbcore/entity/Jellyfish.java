package dev.probablyekho.pbcore.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public class Jellyfish extends Mob {
    public float xBodyRot;
    public float xBodyRotO;
    public float zBodyRot;
    public float zBodyRotO;
    public float tentacleMovement;
    public float oldTentacleMovement;
    public float tentacleAngle;
    public float oldTentacleAngle;
    private float speed;
    private float tentacleSpeed;
    private float rotateSpeed;
    private float tx;
    private float ty;
    private float tz;

    public Jellyfish(EntityType<? extends dev.probablyekho.pbcore.entity.Jellyfish> entityType, Level level) {
        super(entityType, level);
        this.random.setSeed(this.getId());
        this.tentacleSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new JellyfishFleeGoal());
        this.goalSelector.addGoal(1, new JellyfishRandomMovementGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.GLOW_SQUID_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.GLOW_SQUID_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GLOW_SQUID_DEATH;
    }

    protected SoundEvent getSquirtSound() {
        return SoundEvents.GLOW_SQUID_SQUIRT;
    }

    @Override
    public boolean canBeLeashed() {
        return true;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    public static boolean checkJellyfishSpawnRules(EntityType<Jellyfish> jellyfish, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.canSeeSky(pos) && level.getMoonPhase() == 4 && Monster.isDarkEnoughToSpawn((ServerLevelAccessor) level, pos, random);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.xBodyRotO = this.xBodyRot;
        this.zBodyRotO = this.zBodyRot;
        this.oldTentacleMovement = this.tentacleMovement;
        this.oldTentacleAngle = this.tentacleAngle;
        this.tentacleMovement = this.tentacleMovement + this.tentacleSpeed;
        this.setAirSupply(this.getMaxAirSupply());
        if ((double)this.tentacleMovement > Math.PI * 2) {
            if (this.level().isClientSide) {
                this.tentacleMovement = (float) (Math.PI * 2);
            } else {
                this.tentacleMovement -= (float) (Math.PI * 2);
                if (this.random.nextInt(10) == 0) {
                    this.tentacleSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
                }

                this.level().broadcastEntityEvent(this, (byte)19);
            }
        }
        if (this.tentacleMovement < Math.PI) {
            float f = this.tentacleMovement / (float) Math.PI;
            this.tentacleAngle =
                Mth.sin(f * f * (float) Math.PI)
                * (float) Math.PI
                * 0.25F;

            if (f > 0.75F) {
                this.speed = 1.0F;
                this.rotateSpeed = 1.0F;
            } else {
                this.rotateSpeed *= 0.8F;
            }
        } else {
            this.tentacleAngle = 0.0F;
            this.speed *= 0.9F;
            this.rotateSpeed *= 0.99F;
        }
        if (!this.level().isClientSide) {
            if((this.isInWater() || this.getY() < 96) && this.level().canSeeSky(this.blockPosition())) {
                this.setDeltaMovement(
                    this.tx * this.speed,
                    Math.max(this.ty * this.speed, Math.max((96.0D - this.getY()) * 0.01D, 0.05D)),
                    this.tz * this.speed
                );
            } else {
                this.setDeltaMovement(
                    this.tx * this.speed,
                    this.ty * this.speed,
                    this.tz * this.speed
                );
            }
        }
        Vec3 movement = this.getDeltaMovement();
        double horizontalDistance = movement.horizontalDistance();
        this.yBodyRot += (float) ((-Mth.atan2(movement.x, movement.z) * (180.0F / (float) Math.PI) - this.yBodyRot) * 0.1F);
        this.setYRot(this.yBodyRot);
        this.zBodyRot += (float) Math.PI * this.rotateSpeed * 1.5F;
        this.xBodyRot += (float) ((-Mth.atan2(horizontalDistance, movement.y) * (180.0F / (float) Math.PI) - this.xBodyRot) * 0.1F);
        if (!this.level().isClientSide) {
            this.damageNearbyEntities();
        }
        if (this.isAlive() && this.isSunBurnTick()) {
            this.igniteForSeconds(8.0F);
        }
    }

    private void damageNearbyEntities() {
        for (Player player : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(0.2D))) {
            if (player.invulnerableTime <= 0) {
                player.hurt(this.damageSources().mobAttack(this),2.0F);
            }
        }
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (super.hurt(source, amount) && this.getLastHurtByMob() != null) {
            if (!this.level().isClientSide) {
                this.spawnInk();
                LivingEntity livingEntity = this.getLastHurtByMob();
                if(livingEntity.invulnerableTime <= 0 && livingEntity instanceof Player) {
                    livingEntity.hurt(this.damageSources().mobAttack(this), 2.0F);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private Vec3 rotateVector(Vec3 vector) {
        Vec3 vec3 = vector.xRot(this.xBodyRotO * (float) (Math.PI / 180.0));
        return vec3.yRot(-this.yBodyRotO * (float) (Math.PI / 180.0));
    }

    private void spawnInk() {
        this.makeSound(this.getSquirtSound());
        Vec3 vec3 = this.rotateVector(new Vec3(0.0, -1.0, 0.0)).add(this.getX(), this.getY(), this.getZ());

        for (int i = 0; i < 30; i++) {
            Vec3 vec31 = this.rotateVector(new Vec3((double)this.random.nextFloat() * 0.6 - 0.3, -1.0, (double)this.random.nextFloat() * 0.6 - 0.3));
            Vec3 vec32 = vec31.scale(0.3 + (double)(this.random.nextFloat() * 2.0F));
            ((ServerLevel)this.level()).sendParticles(this.getInkParticle(), vec3.x, vec3.y + 0.5, vec3.z, 0, vec32.x, vec32.y, vec32.z, 0.1F);
        }
    }

    protected ParticleOptions getInkParticle() {
        return ParticleTypes.GLOW_SQUID_INK;
    }

    @Override
    public void travel(Vec3 travelVector) {
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 19) {
            this.tentacleMovement = 0.0F;
        } else {
            super.handleEntityEvent(id);
        }
    }

    public void setMovementVector(float tx, float ty, float tz) {
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
    }

    public boolean hasMovementVector() {
        return this.tx != 0.0F || this.ty != 0.0F || this.tz != 0.0F;
    }

    class JellyfishFleeGoal extends Goal {
        private int fleeTicks;

        @Override
        public boolean canUse() {
            LivingEntity livingentity = dev.probablyekho.pbcore.entity.Jellyfish.this.getLastHurtByMob();
            return livingentity != null && Jellyfish.this.distanceToSqr(livingentity) < 100.0;
        }

        @Override
        public void start() {
            this.fleeTicks = 0;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.fleeTicks++;
            LivingEntity livingentity = dev.probablyekho.pbcore.entity.Jellyfish.this.getLastHurtByMob();
            if (livingentity != null) {
                Vec3 vec3 = new Vec3(dev.probablyekho.pbcore.entity.Jellyfish.this.getX() - livingentity.getX(), dev.probablyekho.pbcore.entity.Jellyfish.this.getY() - livingentity.getY(), dev.probablyekho.pbcore.entity.Jellyfish.this.getZ() - livingentity.getZ());
                double d0 = vec3.length();
                if (d0 > 0.0) {
                    vec3.normalize();
                    double d1 = 3.0;
                    if (d0 > 5.0) {
                        d1 -= (d0 - 5.0) / 5.0;
                    }
                    if (d1 > 0.0) {
                        vec3 = vec3.scale(d1);
                    }
                }
                dev.probablyekho.pbcore.entity.Jellyfish.this.setMovementVector((float)vec3.x / 20.0F, (float)vec3.y / 20.0F, (float)vec3.z / 20.0F);
                if (this.fleeTicks % 10 == 5) {
                    dev.probablyekho.pbcore.entity.Jellyfish.this.level().addParticle(ParticleTypes.GLOW, dev.probablyekho.pbcore.entity.Jellyfish.this.getX(), dev.probablyekho.pbcore.entity.Jellyfish.this.getY(), dev.probablyekho.pbcore.entity.Jellyfish.this.getZ(), 0.0, 0.0, 0.0);
                }
            }
        }
    }

    class JellyfishRandomMovementGoal extends Goal {
        private final dev.probablyekho.pbcore.entity.Jellyfish jellyfish;

        public JellyfishRandomMovementGoal(Jellyfish jellyfish) {
            this.jellyfish = jellyfish;
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            int i = this.jellyfish.getNoActionTime();
            if (i > 100) {
                this.jellyfish.setMovementVector(0.0F, 0.0F, 0.0F);
            } else if (this.jellyfish.getRandom().nextInt(reducedTickDelay(75)) == 0 || !this.jellyfish.hasMovementVector()) {
                float f = this.jellyfish.getRandom().nextFloat() * (float) (Math.PI * 2);
                float f1 = Mth.cos(f) * 0.3F;
                float f2 = -0.15F + this.jellyfish.getRandom().nextFloat() * 0.3F;
                float f3 = Mth.sin(f) * 0.3F;
                this.jellyfish.setMovementVector(f1, f2, f3);
            }
        }
    }
}
