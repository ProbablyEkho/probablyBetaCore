package dev.probablyekho.pbcore;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<String> MONSTER_ROOM_COBBLE = BUILDER
            .comment("A block to replace cobblestone in monster rooms with")
            .define("cobblestone", "minecraft:cobblestone");
    public static final ModConfigSpec.ConfigValue<String> MONSTER_ROOM_MOSSY_COBBLE = BUILDER
            .comment("A block to replace mossy cobblestone in monster rooms with")
            .define("mossy_cobblestone", "minecraft:mossy_cobblestone");

    public static BlockState getDungeonCobblestone() {
        return BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(MONSTER_ROOM_COBBLE.get())).defaultBlockState();
    }
    public static BlockState getDungeonMossyCobblestone() {
        return BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(MONSTER_ROOM_MOSSY_COBBLE.get())).defaultBlockState();
    }

    static final ModConfigSpec SPEC = BUILDER.build();
}
