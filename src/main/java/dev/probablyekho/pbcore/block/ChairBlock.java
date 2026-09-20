package dev.probablyekho.pbcore.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class ChairBlock extends StairBlock {
    public ChairBlock(BlockState baseState, Properties properties) {
        super(baseState, properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(blockpos);
        BlockState blockstate = this.defaultBlockState()
            .setValue(FACING, context.getHorizontalDirection())
            .setValue(HALF, Half.BOTTOM)
            .setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
        return blockstate.setValue(SHAPE, StairsShape.STRAIGHT);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return facing.getAxis().isHorizontal()
            ? state.setValue(SHAPE, StairsShape.STRAIGHT)
            : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }
}
