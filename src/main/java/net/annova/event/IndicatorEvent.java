package net.annova.event;

import net.annova.event.custom.BattleCE;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static net.annova.environment.dungeon.Loot.random;
import static net.annova.main.AnnovaRPG.getInstance;

public class IndicatorEvent implements Listener {
    @EventHandler
    public void entity(BattleCE event) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () -> {
            EntityDamageByEntityEvent ev = event.getEvent();
            if (ev.getFinalDamage() < 1) return;

            Location loc = ev.getEntity().getLocation().clone().add(0, 1.5, 0);
            loc.add(random.nextFloat(2) - 1, random.nextFloat(2) - 1, random.nextFloat(2) - 1);
            loc.getWorld().spawn(loc, ArmorStand.class, stand -> {
                stand.setMarker(true);
                stand.setVisible(false);
                stand.setGravity(false);
                stand.setSmall(true);
                stand.setCustomNameVisible(true);
                stand.setCustomName(ChatColor.BOLD + "" + ChatColor.RED + Math.round(ev.getFinalDamage() * 10) / 10);
                Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), stand::remove, 25L);
            });
        }, 1L);
    }
}
