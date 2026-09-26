package dev.probablyekho.pbcore.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class WizardMountainConfig implements FeatureConfiguration {
    public static final Codec<WizardMountainConfig> CODEC = RecordCodecBuilder.create(
        mountainConfigInstance -> mountainConfigInstance.group(
                    IntProvider.CODEC.fieldOf("radius").forGetter(gago_1 -> gago_1.radius),
                    IntProvider.CODEC.fieldOf("height").forGetter(gago_2 -> gago_2.height),
                    FloatProvider.CODEC.fieldOf("depthScale").forGetter(gago_3 -> gago_3.depthScale),
                    IntProvider.CODEC.fieldOf("maxVerticalGap").forGetter(gago_4 -> gago_4.maxVerticalGap),
                    IntProvider.CODEC.fieldOf("depthOffset").forGetter(gago_5 -> gago_5.depthOffset)
                )
                .apply(mountainConfigInstance, WizardMountainConfig::new)
    );
    public final IntProvider radius;
    public final IntProvider height;
    public final FloatProvider depthScale;
    public final IntProvider maxVerticalGap;
    public final IntProvider depthOffset;
    public WizardMountainConfig(IntProvider radius, IntProvider height, FloatProvider depthScale, IntProvider maxVerticalGap, IntProvider depthOffset) {
        this.radius = radius;
        this.height = height;
        this.depthScale = depthScale;
        this.maxVerticalGap = maxVerticalGap;
        this.depthOffset = depthOffset;
    }
}
