package net.annova.environment.world.event;

import net.annova.event.custom.NonCommandBlockRestrictionCE;
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
    @EventHandler
    public void restriction(NonCommandBlockRestrictionCE event) {
        if (worldSectors.containsKey(event.getLevel())) return;
        event.setCancelled(true);
    }

    // получение урона отменяется в RPGPerson
}
