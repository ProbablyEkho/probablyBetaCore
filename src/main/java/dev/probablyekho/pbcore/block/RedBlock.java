package dev.probablyekho.pbcore.block;

import com.mojang.serialization.MapCodec;
import dev.probablyekho.pbcore.entity.PrimedRed;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;

public class RedBlock extends TntBlock {
    public static final MapCodec<TntBlock> CODEC = simpleCodec(RedBlock::new);

    @Override
    public MapCodec<TntBlock> codec() { return CODEC; }

    public RedBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable net.minecraft.core.Direction face, @Nullable LivingEntity entity) {
        if (!level.isClientSide) {
            PrimedRed red = new PrimedRed(level, (double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, entity);
            red.setBlockState(state);
            level.addFreshEntity(red);
            level.playSound(null, red.getX(), red.getY(), red.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 0.25F);
            level.gameEvent(entity, GameEvent.PRIME_FUSE, pos);
        }
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            PrimedRed red = new PrimedRed(
                level, (double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, explosion.getIndirectSourceEntity()
            );
            red.setBlockState(defaultBlockState());
            int i = red.getFuse();
            red.setFuse((short)(level.random.nextInt(i / 4) + i / 8));
            level.addFreshEntity(red);
        }
    }
}
