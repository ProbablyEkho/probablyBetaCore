package dev.probablyekho.pbcore.item;

import dev.probablyekho.pbcore.probablyBetaCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Map;

public class ArmorMaterialRegistry {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, probablyBetaCore.MODID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> QUIVER = ARMOR_MATERIALS.register("quiver",
            () -> new ArmorMaterial(Map.of(
                    ArmorItem.Type.BOOTS, 0,
                    ArmorItem.Type.LEGGINGS, 0,
                    ArmorItem.Type.CHESTPLATE, 0,
                    ArmorItem.Type.HELMET, 0,
                    ArmorItem.Type.BODY, 0
            ), 0, SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.of(Items.ARROW), List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(probablyBetaCore.MODID, "quiver"))), 0, 0
            )
    );

    public static void register(IEventBus eventBus) { ARMOR_MATERIALS.register(eventBus); }
}
