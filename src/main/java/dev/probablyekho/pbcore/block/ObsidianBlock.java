package dev.probablyekho.pbcore.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ObsidianBlock extends Block {

	public ObsidianBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	private void fizz(LevelAccessor levelAccessor, BlockPos blockPos) {
        levelAccessor.levelEvent(1501, blockPos, 0);
    }

	@Override
    protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
		for(Direction direction : Direction.values()) {
			if(direction == Direction.DOWN) {
				continue;
			}
			if(serverLevel.getBlockState(blockPos.relative(direction)).is(Blocks.LAVA)) {
				serverLevel.setBlockAndUpdate(blockPos, BlockRegistry.GLOWING_OBSIDIAN.get().defaultBlockState());
				this.fizz(serverLevel, blockPos);
				return;
			}
		}
    }

	@Override
	protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if(itemStack.is(Items.LAVA_BUCKET)) {
			player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.BUCKET)));
			player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
			level.setBlockAndUpdate(blockPos, BlockRegistry.GLOWING_OBSIDIAN.get().defaultBlockState());
			level.playSound(null, blockPos, SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
		} else {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
	}
}
