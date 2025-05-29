package net.annova.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import static net.annova.main.GlobalRPG.getEntity;

public class SpawnEvent implements Listener {
    @EventHandler
    public void spawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (event.getEntity() instanceof Monster && getEntity(entity) == null) event.setCancelled(true);
    }
}
