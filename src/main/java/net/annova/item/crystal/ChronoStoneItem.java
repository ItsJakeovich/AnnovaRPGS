package net.annova.item.crystal;

import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public class ChronoStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.DARK_PURPLE + "Кристалл старости";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{331};
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fУвеличивает &4⚔ &fна &62% &fза каждое отсутствующее &c\uD83D\uDDA4"
        );
    }

    // drop event in net.annova.event.AgeStonesEvent

    @EventHandler
    private void mechanic(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!hasItem(player)) return;

        // damage * (1 + 0.05 * PH);
        // PH = losed health.
        event.setDamage(event.getDamage()*(1 + 0.02 * (player.getAttribute(Attribute.MAX_HEALTH).getValue() - player.getHealth())));
    }
}
