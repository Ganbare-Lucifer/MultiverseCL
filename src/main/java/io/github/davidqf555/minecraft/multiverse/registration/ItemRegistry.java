package io.github.davidqf555.minecraft.multiverse.registration;

import io.github.davidqf555.minecraft.multiverse.common.Multiverse;
import io.github.davidqf555.minecraft.multiverse.common.items.*;
import io.github.davidqf555.minecraft.multiverse.common.items.tools.MultiversalAxeItem;
import io.github.davidqf555.minecraft.multiverse.common.items.tools.MultiversalPickaxeItem;
import io.github.davidqf555.minecraft.multiverse.common.items.tools.MultiversalShovelItem;
import io.github.davidqf555.minecraft.multiverse.common.items.tools.RiftSwordItem;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public final class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Multiverse.MOD_ID);
    public static final CreativeModeTab TAB = new CreativeModeTab(Multiverse.MOD_ID) {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return KALEIDITE_SHARD.get().getDefaultInstance();
        }
    };

    public static final RegistryObject<SpawnCollectorItem> UNIVERSAL_TREASURE = register("universal_treasure", () -> new SpawnCollectorItem(new Item.Properties().rarity(Rarity.RARE).tab(TAB).fireResistant(), 160));
    public static final RegistryObject<RiftDeathItem> TOTEM_OF_ESCAPE = register("totem_of_escape", () -> new RiftDeathItem(new Item.Properties().stacksTo(1).tab(TAB).rarity(Rarity.UNCOMMON), 5));
    public static final RegistryObject<RiftCoreItem> KALEIDITE_CORE = register("kaleidite_core", () -> new RiftCoreItem(new Item.Properties().tab(TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> KALEIDITE_SHARD = register("kaleidite_shard", () -> new Item(new Item.Properties().tab(TAB)));
    public static final RegistryObject<SimpleLoreItem> MULTIVERSAL_BEACON = register("multiversal_beacon", () -> new SimpleLoreItem(true, ChatFormatting.GOLD, new Item.Properties().tab(TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<ArmorItem> KALEIDITE_HELMET = register("kaleidite_helmet", () -> new ArmorItem(KaleiditeArmorMaterial.INSTANCE, EquipmentSlot.HEAD, new Item.Properties().tab(TAB)));
    public static final RegistryObject<BeaconArmorItem> KALEIDITE_CHESTPLATE = register("kaleidite_chestplate", () -> new BeaconArmorItem(KaleiditeArmorMaterial.INSTANCE, EquipmentSlot.CHEST, new Item.Properties().tab(TAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<ArmorItem> KALEIDITE_LEGGINGS = register("kaleidite_leggings", () -> new ArmorItem(KaleiditeArmorMaterial.INSTANCE, EquipmentSlot.LEGS, new Item.Properties().tab(TAB)));
    public static final RegistryObject<ArmorItem> KALEIDITE_BOOTS = register("kaleidite_boots", () -> new ArmorItem(KaleiditeArmorMaterial.INSTANCE, EquipmentSlot.FEET, new Item.Properties().tab(TAB)));
    public static final RegistryObject<SummonCrossbowItem> KALEIDITE_CROSSBOW = register("kaleidite_crossbow", () -> new SummonCrossbowItem(new Item.Properties().tab(TAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<SwordItem> KALEIDITE_SWORD = register("kaleidite_sword", () -> new SwordItem(KaleiditeItemTier.INSTANCE, 3, -2.4f, new Item.Properties().tab(TAB)));
    public static final RegistryObject<PickaxeItem> KALEIDITE_PICKAXE = register("kaleidite_pickaxe", () -> new PickaxeItem(KaleiditeItemTier.INSTANCE, 1, -2.8f, new Item.Properties().tab(TAB)));
    public static final RegistryObject<ShovelItem> KALEIDITE_SHOVEL = register("kaleidite_shovel", () -> new ShovelItem(KaleiditeItemTier.INSTANCE, 1.5f, -3, new Item.Properties().tab(TAB)));
    public static final RegistryObject<AxeItem> KALEIDITE_AXE = register("kaleidite_axe", () -> new AxeItem(KaleiditeItemTier.INSTANCE, 6, -3.1f, new Item.Properties().tab(TAB)));
    public static final RegistryObject<RiftSwordItem> PRISMATIC_SWORD = register("prismatic_sword", () -> new RiftSwordItem(KaleiditeItemTier.INSTANCE, 4, -2.4f, new Item.Properties().rarity(Rarity.EPIC).tab(TAB)));
    public static final RegistryObject<MultiversalPickaxeItem> PRISMATIC_PICKAXE = register("prismatic_pickaxe", () -> new MultiversalPickaxeItem(KaleiditeItemTier.INSTANCE, 2, -2.8f, new Item.Properties().rarity(Rarity.EPIC).tab(TAB)));
    public static final RegistryObject<MultiversalShovelItem> PRISMATIC_SHOVEL = register("prismatic_shovel", () -> new MultiversalShovelItem(KaleiditeItemTier.INSTANCE, 2.5f, -3, new Item.Properties().rarity(Rarity.EPIC).tab(TAB)));
    public static final RegistryObject<MultiversalAxeItem> PRISMATIC_AXE = register("prismatic_axe", () -> new MultiversalAxeItem(KaleiditeItemTier.INSTANCE, 6, -2.1f, new Item.Properties().rarity(Rarity.EPIC).tab(TAB)));
    public static final RegistryObject<SimpleLoreItem> DIMENSIONAL_PRISM = register("dimensional_prism", () -> new SimpleLoreItem(true, ChatFormatting.GOLD, new Item.Properties().tab(TAB).rarity(Rarity.RARE)));

    public static final RegistryObject<BlockItem> KALEIDITE_CLUSTER = register("kaleidite_cluster", () -> new BlockItem(BlockRegistry.KALEIDITE_CLUSTER.get(), new Item.Properties().tab(TAB)));

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }

    private ItemRegistry() {
    }

}
