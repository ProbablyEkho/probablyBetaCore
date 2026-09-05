package dev.probablyekho.pbcore.item;

import dev.probablyekho.pbcore.block.BlockRegistry;
import dev.probablyekho.pbcore.probablyBetaCore;
import dev.probablyekho.pbcore.sound.SoundRegistry;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(probablyBetaCore.MODID);

    public static final DeferredItem<Item> MUSIC_DISC_ALPHA = ITEMS.register("music_disc_alpha",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(SoundRegistry.ALPHA_KEY)));
    public static final DeferredItem<Item> MUSIC_DISC_DOG = ITEMS.register("music_disc_dog",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(SoundRegistry.DOG_KEY)));
    public static final DeferredItem<Item> MUSIC_DISC_FACE = ITEMS.register("music_disc_face",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(SoundRegistry.FACE_KEY)));
    public static final DeferredItem<Item> MUSIC_DISC_RICO = ITEMS.register("music_disc_rico",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(SoundRegistry.RICO_KEY)));
    public static final DeferredItem<Item> MUSIC_DISC_CALM4 = ITEMS.register("music_disc_calm4",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(SoundRegistry.CALM4_KEY)));
    public static final DeferredItem<Item> MUSIC_DISC_PTERODACTYL = ITEMS.register("music_disc_pterodactyl",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(SoundRegistry.PTERODACTYL_KEY)));

    public static final DeferredItem<Item> FIREBALL = ITEMS.register("fireball",
            () -> new FireballItem(new Item.Properties()));
    public static final DeferredItem<Item> QUIVER = ITEMS.register("quiver",
            () -> new QuiverItem(ArmorMaterialRegistry.QUIVER, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(512)));

	public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
