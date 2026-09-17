package dev.probablyekho.pbcore.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class GhostPumpkinBlock extends Block {
    public static final MapCodec<GhostPumpkinBlock> CODEC = simpleCodec(GhostPumpkinBlock::new);
    @Override
    protected MapCodec<? extends GhostPumpkinBlock> codec() {
        return CODEC;
    }
    public GhostPumpkinBlock(Properties properties) {
        super(properties);
    }
    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (!itemStack.canPerformAction(net.neoforged.neoforge.common.ItemAbilities.SHEARS_CARVE)) {
            return super.useItemOn(itemStack, blockState, level, blockPos, player, hand, blockHitResult);
        } else if (level.isClientSide) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        } else {
            Direction direction = blockHitResult.getDirection();
            Direction direction1 = direction.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : direction;
            level.playSound(null, blockPos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(blockPos, BlockRegistry.CARVED_GHOST_PUMPKIN.get().defaultBlockState().setValue(CarvedPumpkinBlock.FACING, direction1), 11);
            ItemEntity itementity = new ItemEntity(
                level,
                (double)blockPos.getX() + 0.5 + (double)direction1.getStepX() * 0.65,
                (double)blockPos.getY() + 0.1,
                (double)blockPos.getZ() + 0.5 + (double)direction1.getStepZ() * 0.65,
                new ItemStack(Items.PUMPKIN_SEEDS, 4)
            );
            itementity.setDeltaMovement(
                0.05 * (double)direction1.getStepX() + level.random.nextDouble() * 0.02,
                0.05,
                0.05 * (double)direction1.getStepZ() + level.random.nextDouble() * 0.02
            );
            level.addFreshEntity(itementity);
            itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            level.gameEvent(player, GameEvent.SHEAR, blockPos);
            player.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
    }
}
