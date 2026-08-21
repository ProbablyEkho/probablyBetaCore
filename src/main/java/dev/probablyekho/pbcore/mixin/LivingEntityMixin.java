package dev.probablyekho.pbcore.mixin;

import dev.probablyekho.pbcore.probablyBetaCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Unique
    LivingEntity entity = (LivingEntity)(Object)this;

    @Unique
    private static final ResourceLocation HP_SPEED_BOOST_ID = ResourceLocation.fromNamespaceAndPath(probablyBetaCore.MODID, "hp_speed_boost");

    @Inject(method = "setHealth", at = @At("TAIL"))
    private void zombieSpeedBuffFromHealthLoss(float health, CallbackInfo ci) {
        if(!(entity instanceof Zombie zombie)) return;

        AttributeInstance zombieMovementSpeed = zombie.getAttribute(Attributes.MOVEMENT_SPEED);
        if(zombieMovementSpeed == null) return;
        zombieMovementSpeed.removeModifier(HP_SPEED_BOOST_ID);
        float bonusHp = 1.0F - (zombie.getHealth() / zombie.getMaxHealth());
        if(bonusHp > 0) {
            zombieMovementSpeed.addTransientModifier(new AttributeModifier(HP_SPEED_BOOST_ID, bonusHp, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
    }

    @Inject(method = "getVisibilityPercent", at = @At("RETURN"), cancellable = true)
    private void pumpStealth(Entity lookingEntity, CallbackInfoReturnable<Double> cir) {
        if (lookingEntity != null) {
            ItemStack itemStack = entity.getItemBySlot(EquipmentSlot.HEAD);
            if (itemStack.is(Items.CARVED_PUMPKIN)) {
                cir.setReturnValue(cir.getReturnValue() * 0.375);
            }
        }
    }

    /*
    @Inject(method = "hurt", at = @At("RETURN"))
    private void brittlePumps(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack = entity.getItemBySlot(EquipmentSlot.HEAD);
        if(itemStack.is(Items.CARVED_PUMPKIN) && source.getEntity() != null && source.getEntity() instanceof LivingEntity && cir.getReturnValue()) {
            entity.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARMOR_STAND_BREAK, entity.getSoundSource(), 1.0F, 1.0F);
            if (entity.level() instanceof ServerLevel) {
                ((ServerLevel)entity.level()).sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.CARVED_PUMPKIN.defaultBlockState()),
                    entity.getX(),
                    entity.getY(0.6666666666666666),
                    entity.getZ(),
                    10,
                    entity.getBbWidth() / 4.0F,
                    entity.getBbHeight() / 4.0F,
                    entity.getBbWidth() / 4.0F,
                    0.05
                );
            }
        }
    }
    */
}
