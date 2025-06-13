package net.annova.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import static net.annova.main.Keys.NK_ID;

public class SpawnEvent implements Listener {
    @EventHandler
    public void spawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        //if (entity instanceof Monster && !entity.getPersistentDataContainer().has(NK_ID)) event.setCancelled(true);
    }
}
