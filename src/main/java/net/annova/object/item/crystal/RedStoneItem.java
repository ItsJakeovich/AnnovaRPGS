package net.annova.object.item.crystal;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.annova.object.item.base.Item.RED_STONE;

public class RedStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
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
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3| &fУсиливает эффективность колец&3: &a+0.8% &7(макс. +6.4%)"
        );
    }

    private final Map<UUID, ItemStack[]> contents = new HashMap<>();
    @EventHandler
    private void mechanic(PlayerDeathEvent event) {
        ItemStack item = RED_STONE.getFromInventory(event.getEntity());
        if (item == null) return;
        Player player = event.getEntity();
        subtractItem(player, item);
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
