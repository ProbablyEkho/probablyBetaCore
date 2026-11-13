package dev.probablyekho.pbcore.mixin;

import com.evandev.better_straw_beds.block.StrawBedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(StrawBedBlock.class)
public class StrawBedBlockMixin {
    /**
     * @author ProbablyEkho
     * @reason Expands beds' shape a bit   straw bed edition
     */
    @Overwrite
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D);
    }
}
