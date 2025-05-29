package net.annova.item.crystal;

import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public class BalanceStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.GRAY + "Кристалл " + ChatColor.WHITE + "баланса";
    }

    @Override
    public List<Float> modelData() {
        return List.of(316f);
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fУвеличивает вдвое урон по нежити",
                "&3= &fУменьшает вдвое урон по не-нежити"
        );
    }

    @EventHandler
    private void mechanic(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!hasItem(player)) return;

        Entity entity = event.getEntity();
        switch (entity.getType()) {
            case SKELETON:
            case ZOMBIE:
            case HUSK:
            case STRAY:
            case DROWNED:
            case WITHER_SKELETON:
            case ZOMBIFIED_PIGLIN:
            case ZOMBIE_VILLAGER:
                event.setDamage(event.getDamage()*2);
                break;
            default:
                event.setDamage(event.getDamage()/2);
        }
    }
}
