package net.annova.item.token;

import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

import java.util.List;

import static net.annova.util.BukkitObjects.chance;

public class DurabilityTokenItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public String name() {
        return ChatColor.GREEN + "Жетон прочности";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{8};
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fДаёт 15% шанс не потерять прочность предмета"
        );
    }

    @EventHandler
    private void mechanic(PlayerItemDamageEvent event) {
        if (!hasItem(event.getPlayer())) return;

        if (chance(0.15)) event.setCancelled(true);
    }
}
