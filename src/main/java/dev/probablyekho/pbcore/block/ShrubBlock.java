package dev.probablyekho.pbcore.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BushBlock;

public class ShrubBlock extends BushBlock {
    public static final MapCodec<ShrubBlock> CODEC = simpleCodec(ShrubBlock::new);

    protected ShrubBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }
}
