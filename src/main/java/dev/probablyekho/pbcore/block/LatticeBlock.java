package dev.probablyekho.pbcore.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LatticeBlock extends FenceTwoBlock {
    public LatticeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        switch (state.getValue(AXIS)) {
            case Z:
                return Block.box(6.0F, 0.0F, -8.0F, 10.0F, 16.0F, 24.0F);
            case X:
            default:
                return Block.box(-8.0F, 0.0F, 6.0F, 24.0F, 16.0F, 10.0F);
        }
    }
}
