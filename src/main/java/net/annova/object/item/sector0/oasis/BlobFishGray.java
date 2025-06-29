package net.annova.object.item.sector0.oasis;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static net.annova.main.Keys.NK_BLOB_FISH;
import static net.annova.object.item.base.ItemType.BLOB_FISH_BLUE;
import static net.annova.util.BukkitObjects.glowItem;

public class BlobFishGray extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(4f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @EventHandler
    public void consume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (BLOB_FISH_BLUE.equals(item)) {
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            if (meta.getPersistentDataContainer().has(NK_BLOB_FISH)) {
                pdc.set(NK_BLOB_FISH, PersistentDataType.BOOLEAN, false);
                item.setItemMeta(meta);
                event.setCancelled(true);
            } else {
                Player player = event.getPlayer();
                boolean value = pdc.get(NK_BLOB_FISH, PersistentDataType.BOOLEAN);
                if (value) {
                    World world = player.getWorld();
                    if (world.isClearWeather()) event.setCancelled(true);
                    else {
                        world.setThundering(false);
                        world.setStorm(true);
                        world.setThunderDuration(0);
                        world.setClearWeatherDuration(0);
                        world.setWeatherDuration(18000);
                    }
                } else {
                    pdc.set(NK_BLOB_FISH, PersistentDataType.BOOLEAN, true);
                    item.setItemMeta(meta);
                    player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1, 1);
                    glowItem(item);
                    event.setCancelled(true);
                }
            }
        }
    }
}
