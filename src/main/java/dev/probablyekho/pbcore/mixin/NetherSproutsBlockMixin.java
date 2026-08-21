package dev.probablyekho.pbcore.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.NetherSproutsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(NetherSproutsBlock.class)
public abstract class NetherSproutsBlockMixin extends BushBlock {
    protected NetherSproutsBlockMixin(Properties properties) {
        super(properties);
    }

    /**
     * @author ProbablyEkho
     * @reason Makes Sprouts plantable on Netherrack
     */
    @Overwrite
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.NYLIUM) || state.is(Blocks.NETHERRACK) || state.is(Blocks.SOUL_SOIL) || super.mayPlaceOn(state, level, pos);
    }
}
