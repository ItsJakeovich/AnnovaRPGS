package net.annova.environment.world.sector0.location.event;

import net.annova.environment.world.sector0.location.DarkForest;
import net.annova.event.custom.NonCommandBlockRestrictionCE;
import net.annova.object.person.RPGPerson;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;

import static net.annova.environment.world.GlobalWorld.getWorldSector;
import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class DarkForestEvent implements Listener {
    @EventHandler
    public void ignite(BlockIgniteEvent event) {
        Block block = event.getIgnitingBlock();
        if (block != null && getWorldSector(world0).zones()[0].isInside(block.getLocation())) // Dark Forest
            event.setCancelled(true);
    }
    @EventHandler
    public void restrict(BlockBreakEvent event) {
        RPGPerson person = getRPGPerson(event.getPlayer());
        if (person.getSectorLocation() instanceof DarkForest) {
            Material type = event.getBlock().getType();
            if (type == Material.SPRUCE_WOOD || type == Material.BIRCH_WOOD) person.damage(8);
        }
    }
}
