package net.annova.object.item.token;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static net.annova.object.item.base.ItemType.FLY_TOKEN;
import static net.annova.main.AnnovaRPG.getInstance;

public class FlyTokenItem extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(6f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    private static final Set<UUID> allow = new HashSet<>();
    @EventHandler
    private void use(PlayerInteractEvent event) {
        if (event.getAction().name().charAt(0) != 'R') return;
        Player player = event.getPlayer();
        if (!FLY_TOKEN.use(player)) return;

        player.setAllowFlight(true);
        UUID id = player.getUniqueId();
        allow.add(id);
        Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () -> {
            allow.remove(id);
            if (player.isOnline()) player.setAllowFlight(false);
        }, 18000);
    }
    @EventHandler
    private void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if (allow.contains(player.getUniqueId())) return;

        player.setAllowFlight(false);
    }
}
