package dev.probablyekho.pbcore.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FlowerbedBlock extends BushBlock {
    public static final MapCodec<FlowerbedBlock> CODEC = simpleCodec(FlowerbedBlock::new);
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0);

    protected FlowerbedBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
