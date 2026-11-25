package dev.probablyekho.pbcore.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.neoforge.common.util.TriState;

import javax.annotation.Nullable;

public class ChamomileBlock extends Block {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public ChamomileBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
    }

    protected boolean mayPlaceOn(BlockState state) {
        return state.is(BlockTags.DIRT) || state.getBlock() instanceof FarmBlock;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(random.nextInt(5) == 0 && state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            for (int i = 0; i < random.nextInt(1) + 1; i++) {
                level.addParticle(ParticleTypes.SNOWFLAKE, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F, 0.05F, Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F);
            }
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (!state.canSurvive(level, currentPos)) {
            level.scheduleTick(currentPos, this, 1);
        }
        if (facing == Direction.UP) {
            DoubleBlockHalf half = facingState.is(this) ? DoubleBlockHalf.LOWER : DoubleBlockHalf.UPPER;
            if (state.getValue(HALF) != half) {
                state = state.setValue(HALF, half);
            }
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        if (belowState.is(this)) {
            return true;
        }
        TriState soilDecision = belowState.canSustainPlant(level, pos, Direction.UP, state);
        return !soilDecision.isDefault() ? soilDecision.isTrue() : this.mayPlaceOn(belowState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }
}
