package dev.probablyekho.pbcore;

import dev.probablyekho.pbcore.block.BlockRegistry;
import dev.probablyekho.pbcore.entity.EntityRegistry;
import dev.probablyekho.pbcore.entity.PrimedRed;
import dev.probablyekho.pbcore.item.ArmorMaterialRegistry;
import dev.probablyekho.pbcore.item.ItemRegistry;
import dev.probablyekho.pbcore.item.QuiverItem;
import dev.probablyekho.pbcore.sound.SoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(probablyBetaCore.MODID)
public class probablyBetaCore {
    public static final String MODID = "pbcore";
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public probablyBetaCore(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (probablyBetaCore) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        BlockRegistry.register(modEventBus);
        ItemRegistry.register(modEventBus);
        EntityRegistry.register(modEventBus);
        ArmorMaterialRegistry.register(modEventBus);
        SoundRegistry.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        DispenserBlock.registerProjectileBehavior(ItemRegistry.FIREBALL);
        DispenserBlock.registerBehavior(BlockRegistry.RED, new DefaultDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource blockSource, ItemStack itemStack) {
                Level level = blockSource.level();
                BlockPos blockpos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
                PrimedRed primedRed = new PrimedRed(level, (double)blockpos.getX() + 0.5, blockpos.getY(), (double)blockpos.getZ() + 0.25, null);
                primedRed.setBlockState(BlockRegistry.RED.get().defaultBlockState());
                level.addFreshEntity(primedRed);
                level.playSound(null, primedRed.getX(), primedRed.getY(), primedRed.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 0.5F);
                level.gameEvent(null, GameEvent.ENTITY_PLACE, blockpos);
                itemStack.shrink(1);
                return itemStack;
            }
        });
    }

    @SubscribeEvent
    public void explosionResItems(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().removeIf(entity -> entity instanceof ItemEntity);
    }
    @SubscribeEvent
	public void glowingObsidianGen(BlockEvent.FluidPlaceBlockEvent event) {
		if(event.getNewState().is(Blocks.OBSIDIAN)) {
			event.setNewState(BlockRegistry.GLOWING_OBSIDIAN.get().defaultBlockState());
		}
	}
    @SubscribeEvent
    public void quiverArrowShoot(LivingGetProjectileEvent event) {
        ItemStack itemStack = event.getEntity().getItemBySlot(EquipmentSlot.CHEST);
        if(itemStack.getItem() instanceof QuiverItem && QuiverItem.hasArrows(itemStack)) {
            event.setProjectileItemStack(new ItemStack(Items.ARROW));
        }
    }
    @SubscribeEvent
    public void quiverArrowLose(ArrowLooseEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        ItemStack itemStack = event.getEntity().getItemBySlot(EquipmentSlot.CHEST);
        if(itemStack.getItem() instanceof QuiverItem && QuiverItem.hasArrows(itemStack)) {
            QuiverItem.consumeArrow(itemStack);
        }
    }
    @SubscribeEvent
    public void jumpSound(LivingEvent.LivingJumpEvent event) {
        LivingEntity livingEntity = event.getEntity();
        BlockPos blockPos = livingEntity.blockPosition().below();
        SoundType soundtype = livingEntity.level().getBlockState(blockPos).getSoundType(livingEntity.level(), blockPos, livingEntity);
        livingEntity.playSound(soundtype.getStepSound(), (float) (soundtype.getVolume() * 1.1125), (float) (soundtype.getPitch() * 1.25));
    }
    @SubscribeEvent
    public void noTallSeagrass(BonemealEvent event) {
        if(event.getState().is(Blocks.SEAGRASS)) {
            event.setCanceled(true);
        }
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
			event.accept(BlockRegistry.BUSH);
			event.accept(BlockRegistry.OBSIDIAN);
			event.accept(BlockRegistry.GLOWING_OBSIDIAN);
			event.accept(BlockRegistry.NETHER_SULPHUR_ORE);
			event.accept(BlockRegistry.RED);
		}
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			event.accept(ItemRegistry.MUSIC_DISC_ALPHA);
			event.accept(ItemRegistry.MUSIC_DISC_DOG);
			event.accept(ItemRegistry.MUSIC_DISC_FACE);
			event.accept(ItemRegistry.MUSIC_DISC_RICO);
			event.accept(ItemRegistry.MUSIC_DISC_CALM4);
			event.accept(ItemRegistry.MUSIC_DISC_PTERODACTYL);
			event.accept(ItemRegistry.FIREBALL);
		}
		if (event.getTabKey() == CreativeModeTabs.COMBAT) {
			event.accept(ItemRegistry.QUIVER);
		}
		if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
			event.accept(BlockRegistry.WHITE_LAMP);
            event.accept(BlockRegistry.LIGHT_GRAY_LAMP);
            event.accept(BlockRegistry.GRAY_LAMP);
            event.accept(BlockRegistry.BLACK_LAMP);
            event.accept(BlockRegistry.BROWN_LAMP);
            event.accept(BlockRegistry.RED_LAMP);
            event.accept(BlockRegistry.ORANGE_LAMP);
            event.accept(BlockRegistry.YELLOW_LAMP);
            event.accept(BlockRegistry.LIME_LAMP);
            event.accept(BlockRegistry.GREEN_LAMP);
            event.accept(BlockRegistry.LIGHT_BLUE_LAMP);
            event.accept(BlockRegistry.CYAN_LAMP);
            event.accept(BlockRegistry.BLUE_LAMP);
            event.accept(BlockRegistry.PURPLE_LAMP);
            event.accept(BlockRegistry.MAGENTA_LAMP);
            event.accept(BlockRegistry.PINK_LAMP);
		}
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
