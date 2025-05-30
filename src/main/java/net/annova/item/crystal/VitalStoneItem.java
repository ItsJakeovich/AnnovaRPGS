package net.annova.item.crystal;

import net.annova.event.custom.BattleCE;
import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

import static net.annova.item.base.Item.VITAL_STONE;

public class VitalStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.GREEN + "Кристалл молодости";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{323};
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fУвеличивает &4⚔ &fна &61.5% &fза каждое &c\uD83D\uDDA4"
        );
    }

    // drop event in net.annova.event.AgeStonesEvent

    @EventHandler
    private void mechanic(BattleCE event) {
        LivingEntity entity = event.getDamager();
        if (!VITAL_STONE.has(entity)) return;

        // damage * (1 + 0.05 * CH);
        // PH = current health.
        event.increaseDamage(event.getBaseDamage() * 0.0075 * entity.getHealth()); // 1 + 0.015 per heart
    }
}
