package dev.probablyekho.pbcore.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GourdBlock extends Block implements BonemealableBlock {
    public static final MapCodec<GourdBlock> CODEC = simpleCodec(GourdBlock::new);
    protected static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);
    @Override
    protected MapCodec<? extends GourdBlock> codec() {
        return CODEC;
    }
    public GourdBlock(Properties properties) {
        super(properties);
    }
    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }
    @Override
    protected BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos blockPos, BlockPos neighborPos) {
        return !blockState.canSurvive(level, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, neighborState, level, blockPos, neighborPos);
    }
    @Override
    protected boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
        return !level.isEmptyBlock(blockPos.below());
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return pathComputationType == PathComputationType.AIR && !this.hasCollision || super.isPathfindable(state, pathComputationType);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        if(blockState.getBlock() == BlockRegistry.GOURD.get()) {
            serverLevel.setBlock(blockPos, Blocks.PUMPKIN.defaultBlockState(), 2);
        } else {
            serverLevel.setBlock(blockPos, BlockRegistry.GHOST_PUMPKIN.get().defaultBlockState(), 2);
        }
    }
}
