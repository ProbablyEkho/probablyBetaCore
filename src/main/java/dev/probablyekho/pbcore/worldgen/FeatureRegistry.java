package dev.probablyekho.pbcore.worldgen;

import dev.probablyekho.pbcore.probablyBetaCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FeatureRegistry {
    public static final DeferredRegister<Feature<?>> FEATURE_DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.FEATURE, probablyBetaCore.MODID);

    public static final DeferredHolder<Feature<?>, WizardMountainFeature> WIZARD_MOUNTAIN = FEATURE_DEFERRED_REGISTER.register("wizard_mountain", () -> new WizardMountainFeature(NoneFeatureConfiguration.CODEC));

    public static void register(IEventBus eventBus) { FEATURE_DEFERRED_REGISTER.register(eventBus); }
}
