package dev.probablyekho.pbcore.block;

import dev.probablyekho.pbcore.item.ItemRegistry;
import dev.probablyekho.pbcore.probablyBetaCore;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class BlockRegistry {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(probablyBetaCore.MODID);

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

	public static final DeferredBlock<Block> BUSH = registerBlock(
            "bush",
            () -> new ShrubBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().noCollission().instabreak().sound(SoundType.GRASS).ignitedByLava().pushReaction(PushReaction.DESTROY)));
	public static final DeferredBlock<Block> OBSIDIAN = registerBlock(
            "obsidian",
            () -> new ObsidianBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(40.0F, 1200.0F).randomTicks().pushReaction(PushReaction.BLOCK)));
	public static final DeferredBlock<Block> GLOWING_OBSIDIAN = registerBlock(
            "glowing_obsidian",
            () -> new GlowingObsidianBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().lightLevel((p_152684_) -> 15).strength(40.0F, 1200.0F).pushReaction(PushReaction.BLOCK)));
	public static final DeferredBlock<Block> NETHER_SULPHUR_ORE = registerBlock(
            "nether_sulphur_ore",
            () -> new SulphurBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 0.4F).sound(SoundType.NETHERRACK)));
	public static final DeferredBlock<Block> RED = registerBlock(
            "red",
            () -> new RedBlock(BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).instabreak().sound(SoundType.GRASS).ignitedByLava().isRedstoneConductor(BlockRegistry::never)));
	public static final DeferredBlock<CropBlock> FLAX = registerBlock(
            "flax",
            () -> new CropBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY)) {
                @Override
                protected ItemLike getBaseSeedId() {
                    return ItemRegistry.FLAX_SEEDS.get();
                }
            });

    public static final DeferredBlock<Block> WHITE_LAMP = registerBlock("white_lamp", () -> new RedstoneLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Blocks::always)));
    public static final DeferredBlock<Block> LIGHT_GRAY_LAMP = registerBlock("light_gray_lamp", () -> new RedstoneLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Blocks::always)));
    public static final DeferredBlock<Block> GRAY_LAMP = registerBlock("gray_lamp", () -> new RedstoneLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Blocks::always)));
    public static final DeferredBlock<Block> BLACK_LAMP = registerBlock("black_lamp", () -> new RedstoneLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Blocks::always)));
    public static final DeferredBlock<Block> BROWN_LAMP = registerBlock("brown_lamp", () -> new RedstoneLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Blocks::always)));
    public static final DeferredBlock<Block> RED_LAMP = registerBlock("red_lamp", () -> new RedstoneLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Blocks::always)));
    public static final DeferredBlock<Block> ORANGE_LAMP = registerBlock("orange_lamp", () -> new RedstoneLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Blocks::always)));
    public static final DeferredBlock<Block> YELLOW_LAMP = registerBlock("yellow_lamp", () -> new RedstoneLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Blocks::always)));
    public static final DeferredBlock<Block> LIME_LAMP = registerBlock("lime_lamp", () -> new RedstoneLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Blocks::always)));
    public static final DeferredBlock<Block> GREEN_LAMP = registerBlock("green_lamp", () -> new RedstoneLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Blocks::always)));
    public static final DeferredBlock<Block> LIGHT_BLUE_LAMP = registerBlock("light_blue_lamp", () -> new RedstoneLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Blocks::always)));
    public static final DeferredBlock<Block> CYAN_LAMP = registerBlock("cyan_lamp", () -> new RedstoneLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Blocks::always)));
    public static final DeferredBlock<Block> BLUE_LAMP = registerBlock("blue_lamp", () -> new RedstoneLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Blocks::always)));
    public static final DeferredBlock<Block> PURPLE_LAMP = registerBlock("purple_lamp", () -> new RedstoneLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Blocks::always)));
    public static final DeferredBlock<Block> MAGENTA_LAMP = registerBlock("magenta_lamp", () -> new RedstoneLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Blocks::always)));
    public static final DeferredBlock<Block> PINK_LAMP = registerBlock("pink_lamp", () -> new RedstoneLampBlock(BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(Blocks::always)));

    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }
    private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return p_50763_ -> p_50763_.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    public static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
