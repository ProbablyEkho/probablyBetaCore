package dev.probablyekho.pbcore.mixin;

import dev.probablyekho.pbcore.block.BlockRegistry;
import dev.probablyekho.pbcore.entity.PrimedRed;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow private Level level;
    @Unique
    Entity entity = (Entity)(Object)this;


    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    private void pushableTnt(CallbackInfoReturnable<Boolean> cir) {
        if(entity instanceof PrimedTnt) cir.setReturnValue(true);
    }

    @Inject(method = "interactAt", at = @At("HEAD"), cancellable = true)
    private void defuseTnt(Player player, Vec3 vec3, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if(!(entity instanceof PrimedTnt primedTnt)) return;

        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.SHEARS) && !primedTnt.level().isClientSide) {
            primedTnt.discard();
            level.playSound(null, primedTnt.getX(), primedTnt.getY(), primedTnt.getZ(), SoundEvents.SHEEP_SHEAR, primedTnt.getSoundSource());
            level.playSound(null, primedTnt.getX(), primedTnt.getY(), primedTnt.getZ(), SoundEvents.LAVA_EXTINGUISH, primedTnt.getSoundSource());
            ItemStack droppedItem;

            if(primedTnt instanceof PrimedRed) droppedItem = new ItemStack(BlockRegistry.RED.get());
            else droppedItem = new ItemStack(Items.TNT);

            Block.popResource(level, primedTnt.blockPosition(), droppedItem);
            itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            level.gameEvent(GameEvent.SHEAR, primedTnt.position(), GameEvent.Context.of(player));
            player.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
            player.swing(hand, true);
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
        }
    }
}
