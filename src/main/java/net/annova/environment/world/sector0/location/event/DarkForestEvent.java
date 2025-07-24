package net.annova.environment.world.sector0.location.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;

import static net.annova.environment.world.GlobalWorld.worldSectors;
import static net.annova.environment.world.sector0.SuruWahare.world0;

public class DarkForestEvent implements Listener {
    @EventHandler
    public void ignite(BlockIgniteEvent event) {
        if (worldSectors.get(world0).zones()[0].isInside(event.getIgnitingBlock().getLocation())) // Dark Forest
            event.setCancelled(true);
    }
}
