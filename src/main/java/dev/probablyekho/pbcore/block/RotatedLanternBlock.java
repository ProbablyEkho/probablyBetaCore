package dev.probablyekho.pbcore.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RotatedLanternBlock extends LanternBlock implements SimpleWaterloggedBlock {
    private static final int ROTATIONS = RotationSegment.getMaxSegmentIndex() + 1;
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    protected static final VoxelShape AABB = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D);
    protected static final VoxelShape HANGING_AABB = Block.box(4.0D, 8.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    public RotatedLanternBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HANGING, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE).setValue(ROTATION, 0));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
      FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
      for(Direction direction : context.getNearestLookingDirections()) {
         if (direction.getAxis() == Direction.Axis.Y) {
            BlockState blockstate = this.defaultBlockState().setValue(HANGING, direction == Direction.UP);
            if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
               return blockstate.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER).setValue(ROTATION, RotationSegment.convertToSegment(context.getRotation()));
            }
         }
      }
      return null;
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return state.getValue(HANGING) ? HANGING_AABB : AABB;
   }

   @Override
   public BlockState rotate(BlockState state, Rotation rotation) {
      return state.setValue(ROTATION, rotation.rotate(state.getValue(ROTATION), ROTATIONS));
   }

   @Override
   public BlockState mirror(BlockState state, Mirror mirror) {
      return state.setValue(ROTATION, mirror.mirror(state.getValue(ROTATION), ROTATIONS));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(HANGING, WATERLOGGED, ROTATION);
   }

   @Override
   public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
      if(state.getValue(WATERLOGGED)) {
         level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
      }
      return getConnectedDirection(state).getOpposite() == direction && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
   }

   @Override
   public FluidState getFluidState(BlockState state) {
      return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
   }
}
