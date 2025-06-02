package net.annova.object.item.token;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

import java.util.List;

import static net.annova.object.item.base.Item.DURABILITY_TOKEN;
import static net.annova.util.BukkitObjects.chance;

public class DurabilityTokenItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(8f);
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
    public List<String> lore() {
        return List.of(
                "&3= &fДаёт 15% шанс не потерять прочность предмета"
        );
    }

    @EventHandler
    private void mechanic(PlayerItemDamageEvent event) {
        if (!DURABILITY_TOKEN.has(event.getPlayer())) return;

        if (chance(0.15)) event.setCancelled(true);
    }
}
