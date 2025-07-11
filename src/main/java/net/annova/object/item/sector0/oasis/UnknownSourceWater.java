package net.annova.object.item.sector0.oasis;

import net.annova.object.item.base.attribute.ItemConfiguration;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomData;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.environment.world.GlobalWorld.isNight;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.Keys.*;
import static net.annova.object.item.base.ItemType.UNKNOWN_SOURCE_WATER;

public class UnknownSourceWater extends RPGItem implements ItemConfiguration, Listener {
    @Override
    public net.minecraft.world.item.Item type() {
        return Items.POTION;
    }

    @Override
    public List<Float> modelData() {
        return List.of(411f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public void initConfiguration(net.minecraft.world.item.ItemStack item) {
        CustomData data = item.get(DataComponents.CUSTOM_DATA);
        item.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        CompoundTag tag = data.getUnsafe();
        tag.putLong(NK_EXPIRE.toString(), System.currentTimeMillis() + 3600_000);
        tag.putBoolean(NK_SOURCE_WATER.toString(), false);
    }

    private static final PotionEffect defRegeneration = new PotionEffect(PotionEffectType.REGENERATION, 100, 2);
    private static final PotionEffect defLevitation = new PotionEffect(PotionEffectType.LEVITATION, 40, 0);
    private static final PotionEffect MOONLIGHT_REGENERATION = new PotionEffect(PotionEffectType.REGENERATION, 100, 4);
    private static final PotionEffect MOONLIGHT_NIGHTVISION = new PotionEffect(PotionEffectType.NIGHT_VISION, 1200, 0);
    @EventHandler
    public void consume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (UNKNOWN_SOURCE_WATER.equals(item)) {
            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
            if (System.currentTimeMillis() > pdc.get(NK_MILLIS, PersistentDataType.LONG)) return;

            Player player = event.getPlayer();
            if (pdc.get(NK_SOURCE_WATER, PersistentDataType.BOOLEAN)) {
                player.addPotionEffect(defRegeneration);
                player.addPotionEffect(defLevitation);
            } else {
                player.addPotionEffect(MOONLIGHT_REGENERATION);
                player.addPotionEffect(MOONLIGHT_NIGHTVISION);
            }
        }
    }

    @EventHandler
    public void drop(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        ItemStack stack = item.getItemStack();
        if (UNKNOWN_SOURCE_WATER.equals(stack)) {
            Bukkit.getScheduler().runTaskLater(getInstance(), () -> {
                if (!item.isValid() || item.isDead()) return;

                ItemMeta meta = stack.getItemMeta();
                if (isNight(item.getWorld())) {
                    meta.getPersistentDataContainer().set(NK_SOURCE_WATER, PersistentDataType.BOOLEAN, true);
                    stack.addUnsafeEnchantment(Enchantment.LOOTING, 1);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                } else {
                    meta.getPersistentDataContainer().set(NK_EXPIRE, PersistentDataType.LONG, System.currentTimeMillis());
                }
                stack.setItemMeta(meta);
                item.setItemStack(stack);
            }, 2400);
        }
    }
}
