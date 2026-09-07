package dev.probablyekho.pbcore.sound;

import dev.probablyekho.pbcore.probablyBetaCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, probablyBetaCore.MODID);

    private static ResourceKey<JukeboxSong> createSong(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(probablyBetaCore.MODID, name));
    }

    public static final Supplier<SoundEvent> ALPHA = registerSoundEvent("alpha");
    public static final ResourceKey<JukeboxSong> ALPHA_KEY = createSong("alpha");

    public static final Supplier<SoundEvent> DOG = registerSoundEvent("dog");
    public static final ResourceKey<JukeboxSong> DOG_KEY = createSong("dog");

    public static final Supplier<SoundEvent> FACE = registerSoundEvent("face");
    public static final ResourceKey<JukeboxSong> FACE_KEY = createSong("face");

    public static final Supplier<SoundEvent> RICO = registerSoundEvent("rico");
    public static final ResourceKey<JukeboxSong> RICO_KEY = createSong("rico");

    public static final Supplier<SoundEvent> CALM4 = registerSoundEvent("calm4");
    public static final ResourceKey<JukeboxSong> CALM4_KEY = createSong("calm4");

    public static final Supplier<SoundEvent> PTERODACTYL = registerSoundEvent("pterodactyl");
    public static final ResourceKey<JukeboxSong> PTERODACTYL_KEY = createSong("pterodactyl");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(probablyBetaCore.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
