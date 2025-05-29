package net.annova.item.crystal;

import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.annova.item.base.Item.RED_STONE;
import static net.annova.util.BukkitObjects.chance;

public class RedStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.RED + "Красный кристалл";
    }

    @Override
    public List<Float> modelData() {
        return List.of(300f);
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fПосле смерти сохраняет инвентарь"
        );
    }

    @EventHandler
    private void drop(EntityDeathEvent event) {
        if (!chance(0.01)) return;
        event.getDrops().add(RED_STONE.create());
    }

    private final Map<UUID, ItemStack[]> contents = new HashMap<>();
    @EventHandler
    private void mechanic(PlayerDeathEvent event) {
        ItemStack item = RED_STONE.getFromInventory(event.getEntity());
        if (item == null) return;
        Player player = event.getEntity();
        this.removeItem(player, item);
        this.contents.put(player.getUniqueId(), player.getInventory().getContents());
        event.getDrops().clear();
    }
    @EventHandler
    private void revive(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();
        if (!contents.containsKey(id)) return;
        player.getInventory().setContents(contents.get(id));
        this.contents.remove(id);
    }
}
