package net.annova.environment.dungeon;

import net.annova.main.AnnovaAPI;
import net.annova.object.item.base.ItemType;
import net.annova.object.item.base.ItemCategory;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;

import java.util.ArrayList;
import java.util.List;

import static net.annova.main.AnnovaAPI.getMinecraftServer;
import static net.annova.object.item.base.ItemType.*;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;
import static net.minecraft.core.component.DataComponents.CUSTOM_NAME;
import static net.minecraft.world.effect.MobEffects.*;

public class Loot {
    private final int rarity;

    public Loot(int rarity) {
        this.rarity = rarity;
    }

    public int getRarity() {
        return rarity;
    }

    public SimpleContainer getLoot() {
        SimpleContainer container = new SimpleContainer(27);

        for (net.minecraft.world.item.ItemStack item : Dungeon.values()[rarity].getLootItems()) {
            if (item.getItem() == Items.AIR) continue;
            int slot;
            while (true) {
                slot = random.nextInt(27);
                if (container.getItem(slot) != net.minecraft.world.item.ItemStack.EMPTY) continue;
                break;
            }
            container.setItem(slot, item);
        }

        return container;
    }

    public enum Dungeon {
        PRISON,
        ;

        public List<net.minecraft.world.item.ItemStack> getLootItems() {
            switch (this) {
                case PRISON -> {
                    List<net.minecraft.world.item.ItemStack> items = new ArrayList<>(List.of(
                            createItem(Items.DIAMOND, random.nextInt(9)),
                            createItem(Items.GOLD_INGOT, random.nextInt(17)),
                            createPotion(1, 0.3, new MobEffectInstance(INSTANT_HEALTH, 1, 0)),
                            createPotion(1, 0.3, new MobEffectInstance(SPEED, 3600, 0)),
                            createItem(Items.GOLDEN_APPLE, random.nextInt(4) + 1, 0.1),
                            createItem(Items.COOKED_BEEF, random.nextInt(4) + 1),
                            PRISON_TOKEN.create(random.nextInt(3)),
                            PRISON_SWORD.create(0.02),
                            createItem(Items.EMERALD, random.nextInt(6)),
                            createItem(Items.SHULKER_SHELL, random.nextInt(4) + 1, 0.4),
                            createItem(Items.NETHERITE_INGOT, 1, 0.2),

                            createPotion(Component.literal("Зелье адреналина").withColor(Color.YELLOW.asRGB()), 1, 0.01, new MobEffectInstance(STRENGTH, 600, 0), new MobEffectInstance(REGENERATION, 200, 1)),
                            createPotion(Component.literal("Зелье защиты").withColor(Color.GRAY.asRGB()), 1, 0.01, new MobEffectInstance(WEAKNESS, 6000, 4), new MobEffectInstance(RESISTANCE, 6000, 4)),
                            createPotion(Component.literal("Зелье счастья").withColor(Color.GREEN.asRGB()), 1, 0.01, new MobEffectInstance(LUCK, 36000, 2)),
                            createPotion(Component.literal("Зелье небесной силы").withColor(Color.AQUA.asRGB()), 1, 0.01, new MobEffectInstance(SLOW_FALLING, 1200, 0), new MobEffectInstance(STRENGTH, 300, 2)),

                            createItem(Items.HEAVY_CORE, 0.003),
                            createItem(Items.TOTEM_OF_UNDYING, 0.0018),

                            createItem(Items.SPAWNER, 0.0098),
                            createItem(Items.SPAWNER, 0.0049),
                            createItem(Items.CREEPER_SPAWN_EGG, 0.0014),
                            createItem(Items.ZOMBIE_SPAWN_EGG, 0.0014),
                            createItem(Items.SKELETON_SPAWN_EGG, 0.0014),
                            createItem(Items.VINDICATOR_SPAWN_EGG, 0.0014),
                            createItem(Items.EVOKER_SPAWN_EGG, 0.0014),
                            createItem(Items.STRAY_SPAWN_EGG, 0.0014),
                            createItem(Items.PIG_SPAWN_EGG, 0.0014),
                            createItem(Items.COW_SPAWN_EGG, 0.0014),
                            createItem(Items.IRON_GOLEM_SPAWN_EGG, 0.0014),
                            createItem(Items.SPIDER_SPAWN_EGG, 0.0014),
                            createItem(Items.DROWNED_SPAWN_EGG, 0.0014),

                            createBook(Enchantments.SHARPNESS, 6, 0.004),
                            createBook(Enchantments.LOOTING, 4, 0.004),
                            createBook(Enchantments.FORTUNE, 5, 0.004),
                            createBook(Enchantments.EFFICIENCY, 6, 0.004),
                            createBook(Enchantments.UNBREAKING, 5, 0.004),
                            createBook(Enchantments.FEATHER_FALLING, 6, 0.004),
                            createBook(Enchantments.MULTISHOT, 2, 0.004),
                            createBook(Enchantments.SMITE, 7, 0.004),
                            createBook(Enchantments.BANE_OF_ARTHROPODS, 7, 0.004),
                            createBook(Enchantments.DEPTH_STRIDER, 5, 0.004),
                            createBook(Enchantments.LUCK_OF_THE_SEA, 5, 0.004),
                            createBook(Enchantments.LURE, 5, 0.004),
                            createBook(Enchantments.QUICK_CHARGE, 10, 0.004),
                            createBook(Enchantments.MENDING, 2, 0.004)
                    ));
                    for (ItemType itemType : getItemsByCategory(ItemCategory.CRYSTALL)) {
                        if (itemType == TIME_STONE) continue;
                        double chance = switch (itemType) {
                            case RUNIC_STONE, RUNIC_DEFENCE_STONE -> 0.002;
                            default -> 0.01;
                        };

                        items.add(itemType.create(chance));
                    }
                    return items;
                }
            }
            return null;
        }
    }

    public static void dropItem(Item material, Location location, double chance) {
        dropItem(material, 1, location, chance);
    }
    public static void dropItem(Item material, int amount, Location location, double chance) {
        if (chance(chance)) {
            dropItem(material, amount, location);
        }
    }
    public static void dropItem(Item material, int amount, Location location) {
        double xs = Mth.nextDouble(AnnovaAPI.random, -0.25F, 0.25F);
        double ys = Mth.nextDouble(AnnovaAPI.random, -0.25F, 0.25F) - (double) EntityType.ITEM.getHeight() / (double)2.0F;
        double zs = Mth.nextDouble(AnnovaAPI.random, -0.25F, 0.25F);
        Location loc = location.clone().add(xs, ys, zs);
        ServerLevel level = ((CraftWorld) loc.getWorld()).getHandle();
        level.addFreshEntityWithPassengers(new ItemEntity(level, loc.getX(), loc.getY(), loc.getZ(), createItem(material, amount)));
    }
    public static ItemStack createItem(Item material) {
        return new ItemStack(material);
    }
    public static ItemStack createItem(Item material, Color color) {
        ItemStack leatherArmor = new ItemStack(material);
        leatherArmor.set(DataComponents.DYED_COLOR, new DyedItemColor(color.asARGB()));

        return leatherArmor;
    }
    public static ItemStack createItem(Item material, Component name) {
        return createItem(material, name, -1, 1, 1.00);
    }
    public static ItemStack createItem(Item material, int amount) {
        return createItem(material, null, -1, amount, 1.00);
    }
    public static ItemStack createItem(Item material, double chance) {
        return createItem(material, null, -1, 1, chance);
    }
    public static ItemStack createItem(Item material, int amount, double chance) {
        return createItem(material, null, -1, amount, chance);
    }
    public static ItemStack createItem(Item material, Component name, float model, int amount, double chance) {
        if (amount < 1 || !chance(chance)) return ItemStack.EMPTY;
        ItemStack item = new ItemStack(material, amount);
        if (name != null) item.set(CUSTOM_NAME, name);
        if (model != -1) item.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(model), List.of(), List.of(), List.of()));
        return item;
    }

    public static ItemStack createPotion(int amount, MobEffectInstance... effects) {
        return createPotion(null, amount, 1.0, effects);
    }
    public static ItemStack createPotion(int amount, double chance, MobEffectInstance... effects) {
        return createPotion(null, amount, chance, effects);
    }
    public static ItemStack createPotion(Component name, int amount, double chance, MobEffectInstance... effects) {
        if (amount < 1 || !chance(chance)) return ItemStack.EMPTY;
        ItemStack potion = new ItemStack(Items.POTION, amount); // Основной тип - обычное зелье

        if (name != null) potion.set(CUSTOM_NAME, name);
        PotionContents contents = potion.get(DataComponents.POTION_CONTENTS);
        for (MobEffectInstance effect : effects) {
            contents = contents.withEffectAdded(effect);
        }

        return potion;
    }

    public static ItemStack createBook(ResourceKey<net.minecraft.world.item.enchantment.Enchantment> enchantment, int level, double chance) {
       return createBook(null, enchantment, level, chance);
    }
    public static ItemStack createBook(Component name, ResourceKey<net.minecraft.world.item.enchantment.Enchantment> enchantment, int level, double chance) {
        if (!chance(chance)) return ItemStack.EMPTY;
        ItemStack item = EnchantmentHelper.createBook(new EnchantmentInstance(getMinecraftServer().registryAccess().getOrThrow(Enchantments.MENDING), 2));

        if (name != null) item.set(CUSTOM_NAME, name);
        return item;
    }
}
