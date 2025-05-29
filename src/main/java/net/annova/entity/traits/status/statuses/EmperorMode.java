package net.annova.entity.traits.status.statuses;

import net.annova.entity.traits.status.Status;
import net.annova.object.person.RPGPerson;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import static net.annova.main.GlobalRPG.getDungeonWorld;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class EmperorMode implements Listener {
    // если эта шняга не работать, то сделай новый эвент и подключи его к EntityDamageEvent
    @EventHandler(priority = EventPriority.LOWEST)
    private void damage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        RPGPerson person = getRPGPerson(player);
        if (!getRPGPerson(player).containStatus(Status.Type.NETHER_EMPEROR)) return;

        if (player.getWorld().getEnvironment().equals(World.Environment.NETHER)) event.setCancelled(true);
        else event.setDamage(event.getDamage()*0.2);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void death(PlayerDeathEvent event) {
        Player player = event.getEntity();
        RPGPerson person = getRPGPerson(player);
        if (person.getStatus(Status.Type.NETHER_EMPEROR) == null) return;

        World world = player.getWorld();
        if (!world.equals(getDungeonWorld())) return;

        event.setDeathMessage(null);
        Location loc = player.getLocation();
        world.createExplosion(loc, 8, false, false);
        for (Entity entity : world.getNearbyEntities(loc, 18, 18, 18, entity -> entity instanceof Player)) {
            if (entity.equals(player)) continue;
            entity.setFireTicks(600); // 30 seconds
        }

        player.setHealth(20);
        player.setFoodLevel(0);
    }
}
