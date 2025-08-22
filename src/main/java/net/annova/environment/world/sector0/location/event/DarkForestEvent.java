package net.annova.environment.world.sector0.location.event;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;

import static net.annova.environment.world.GlobalWorld.getWorldSector;
import static net.annova.environment.world.sector0.SuruWahare.world0;

public class DarkForestEvent implements Listener {
    @EventHandler
    public void ignite(BlockIgniteEvent event) {
        Block block = event.getIgnitingBlock();
        if (block != null && getWorldSector(world0).zones()[0].isInside(block.getLocation())) // Dark Forest
            event.setCancelled(true);
    }
}
