package dev.probablyekho.pbcore.entity;

import dev.probablyekho.pbcore.probablyBetaCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE_DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, probablyBetaCore.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<PrimedRed>> PRIMED_RED =
            ENTITY_TYPE_DEFERRED_REGISTER.register("primed_red",
                    () -> EntityType.Builder.<PrimedRed>of(PrimedRed::new, MobCategory.MISC)
                            .fireImmune()
                            .sized(0.98F, 0.98F)
                            .clientTrackingRange(10)
                            .updateInterval(10)
                            .build("primed_red"));

    public static void register(IEventBus eventBus) { ENTITY_TYPE_DEFERRED_REGISTER.register(eventBus); }
}
