package net.annova.environment.world.event;

import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import static net.annova.environment.world.GlobalWorld.worldSectors;

public class WorldEvent implements Listener {
    @EventHandler
    public void drop(PlayerDropItemEvent event) {
        if (worldSectors.containsKey(((CraftWorld) event.getPlayer().getWorld()).getHandle())) return;
        event.setCancelled(true);
    }

    /** Событие отмены взаимодействия в {@link net.annova.event.BlockEvent} */

    // получение урона отменяется в RPGPerson
}
