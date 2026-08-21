package dev.probablyekho.pbcore.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CarpetBlock.class)
public class CarpetBlockMixin extends Block {
    public CarpetBlockMixin(Properties properties) {
        super(properties);
    }

    /**
    * @author ProbablyEkho
    * @reason Thickens carpets a bit
    */
    @Overwrite
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
   }
}
