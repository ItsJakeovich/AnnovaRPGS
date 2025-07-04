package net.annova.environment.dungeon;

import net.annova.object.item.base.ItemType;
import net.annova.object.item.base.ItemCategory;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

import static net.annova.object.item.base.ItemType.*;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class Loot {
    private final int rarity;

    public Loot(int rarity) {
        this.rarity = rarity;
    }

    public int getRarity() {
        return rarity;
    }

    public Inventory getLoot() {
        Inventory inventory = Bukkit.createInventory(null, 27);

        for (ItemStack item : Dungeon.values()[rarity].getLootItems()) {
            if (item.getType().equals(Material.AIR)) continue;
            int slot;
            while (true) {
                slot = random.nextInt(27);
                if (inventory.getItem(slot) != null) continue;
                break;
            }
            inventory.setItem(slot, item);
        }

        return inventory;
    }

    public enum Dungeon {
        PRISON,
        ;

        public List<ItemStack> getLootItems() {
            switch (this) {
                case PRISON -> {
                    List<ItemStack> items = new ArrayList<>(List.of(
                            createItem(Material.DIAMOND, random.nextInt(9)),
                            createItem(Material.GOLD_INGOT, random.nextInt(17)),
                            createPotion(1, 0.3, new PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 0)),
                            createPotion(1, 0.3, new PotionEffect(PotionEffectType.SPEED, 3600, 0)),
                            createItem(Material.GOLDEN_APPLE, random.nextInt(4) + 1, 0.1),
                            createItem(Material.COOKED_BEEF, random.nextInt(4) + 1),
                            PRISON_TOKEN.create(random.nextInt(3)),
                            PRISON_SWORD.create(0.02),
                            createItem(Material.EMERALD, random.nextInt(6)),
                            createItem(Material.SHULKER_SHELL, random.nextInt(4) + 1, 0.4),
                            createItem(Material.NETHERITE_INGOT, 1, 0.2),

                            createPotion("§eЗелье адреналина", 1, 0.01, new PotionEffect(PotionEffectType.STRENGTH, 600, 0), new PotionEffect(PotionEffectType.REGENERATION, 200, 1)),
                            createPotion("§7Зелье защиты", 1, 0.01, new PotionEffect(PotionEffectType.WEAKNESS, 6000, 4), new PotionEffect(PotionEffectType.RESISTANCE, 6000, 4)),
                            createPotion("§aЗелье счастья", 1, 0.01, new PotionEffect(PotionEffectType.LUCK, 36000, 2)),
                            createPotion("§bЗелье небесной силы", 1, 0.01, new PotionEffect(PotionEffectType.SLOW_FALLING, 1200, 0), new PotionEffect(PotionEffectType.STRENGTH, 300, 2)),

                            createItem(Material.HEAVY_CORE, 0.003),
                            createItem(Material.TOTEM_OF_UNDYING, 0.0018),

                            createItem(Material.SPAWNER, 0.0098),
                            createItem(Material.SPAWNER, 0.0049),
                            createItem(Material.CREEPER_SPAWN_EGG, 0.0014),
                            createItem(Material.ZOMBIE_SPAWN_EGG, 0.0014),
                            createItem(Material.SKELETON_SPAWN_EGG, 0.0014),
                            createItem(Material.VINDICATOR_SPAWN_EGG, 0.0014),
                            createItem(Material.EVOKER_SPAWN_EGG, 0.0014),
                            createItem(Material.STRAY_SPAWN_EGG, 0.0014),
                            createItem(Material.PIG_SPAWN_EGG, 0.0014),
                            createItem(Material.COW_SPAWN_EGG, 0.0014),
                            createItem(Material.IRON_GOLEM_SPAWN_EGG, 0.0014),
                            createItem(Material.SPIDER_SPAWN_EGG, 0.0014),
                            createItem(Material.DROWNED_SPAWN_EGG, 0.0014),

                            createBook(Enchantment.SHARPNESS, 6, 0.004),
                            createBook(Enchantment.LOOTING, 4, 0.004),
                            createBook(Enchantment.FORTUNE, 5, 0.004),
                            createBook(Enchantment.EFFICIENCY, 6, 0.004),
                            createBook(Enchantment.UNBREAKING, 5, 0.004),
                            createBook(Enchantment.FEATHER_FALLING, 6, 0.004),
                            createBook(Enchantment.MULTISHOT, 2, 0.004),
                            createBook(Enchantment.SMITE, 7, 0.004),
                            createBook(Enchantment.BANE_OF_ARTHROPODS, 7, 0.004),
                            createBook(Enchantment.DEPTH_STRIDER, 5, 0.004),
                            createBook(Enchantment.LUCK_OF_THE_SEA, 5, 0.004),
                            createBook(Enchantment.LURE, 5, 0.004),
                            createBook(Enchantment.QUICK_CHARGE, 10, 0.004),
                            createBook(Enchantment.MENDING, 2, 0.004)
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

    public static void dropItem(Material material, Location location, double chance) {
        dropItem(material, 1, location, chance);
    }
    public static void dropItem(Material material, int amount, Location location, double chance) {
        if (chance(chance)) {
            dropItem(material, amount, location);
        }
    }
    public static void dropItem(Material material, int amount, Location location) {
        location.getWorld().dropItemNaturally(location, createItem(material, amount));
    }
    public static ItemStack createItem(Material material) {
        return new ItemStack(material);
    }
    public static ItemStack createItem(Material material, Color color) {
        if (!material.name().contains("LEATHER")) return null;
        ItemStack leatherArmor = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) leatherArmor.getItemMeta();

        meta.setColor(color);
        leatherArmor.setItemMeta(meta);

        return leatherArmor;
    }
    public static ItemStack createItem(Material material, String name) {
        return createItem(material, name, -1, 1, 1.00);
    }
    public static ItemStack createItem(Material material, int amount) {
        return createItem(material, null, -1, amount, 1.00);
    }
    public static ItemStack createItem(Material material, double chance) {
        return createItem(material, null, -1, 1, chance);
    }
    public static ItemStack createItem(Material material, int amount, double chance) {
        return createItem(material, null, -1, amount, chance);
    }
    public static ItemStack createItem(Material material, String name, float model, int amount, double chance) {
        if (amount < 1 || !chance(chance)) return new ItemStack(Material.AIR);
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (name != null) meta.setDisplayName(name);
        if (model != -1) meta.getCustomModelDataComponent().setFloats(List.of(model));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createPotion(int amount, PotionEffect... effects) {
        return createPotion(null, amount, 1.0, effects);
    }
    public static ItemStack createPotion(int amount, double chance, PotionEffect... effects) {
        return createPotion(null, amount, chance, effects);
    }
    public static ItemStack createPotion(String name, int amount, double chance, PotionEffect... effects) {
        if (amount < 1 || !chance(chance)) return new ItemStack(Material.AIR);
        ItemStack potion = new ItemStack(Material.POTION, amount); // Основной тип - обычное зелье
        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        if (name != null) meta.setDisplayName(name);
        for (PotionEffect effect : effects) {
            meta.addCustomEffect(effect, true);
        }

        potion.setItemMeta(meta);

        return potion;
    }

    public static ItemStack createBook(Enchantment enchantment, int level, double chance) {
       return createBook(null, enchantment, level, chance);
    }
    public static ItemStack createBook(String name, Enchantment enchantment, int level, double chance) {
        if (!chance(chance)) return new ItemStack(Material.AIR);
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(enchantment, level, true);
        if (name != null) meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
