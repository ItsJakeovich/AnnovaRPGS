package net.annova.object.item.sector0.oasis;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.event.CapitatorEvent.Capitator;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.item.base.ItemType.BARK_BEETLE_FISH;

public class BarkBeetleFish extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(14f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fПри попадании на дерево срубает его"
        );
    }

    @EventHandler
    public void capitatorEvent(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        if (!BARK_BEETLE_FISH.equals(item.getItemStack())) return;

        new BukkitRunnable() {
            private final Player player = event.getPlayer();
            private Location location = item.getLocation();
            public void run() {
                Location newLocation = item.getLocation();
                if (!item.isValid() || location.equals(newLocation) || Capitator(player, newLocation.add(newLocation.getDirection().normalize()))) {
                    this.cancel();
                    item.remove();
                    return;
                }
                this.location = newLocation;
            }
        }.runTaskTimer(getInstance(), 0L, 10L);
    }
}
