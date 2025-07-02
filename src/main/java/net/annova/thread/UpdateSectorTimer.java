package net.annova.thread;

import net.annova.environment.world.GlobalWorld;
import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.WorldSector;
import net.annova.thread.event.HourTickEvent;
import net.annova.thread.event.MinuteTickEvent;
import net.annova.thread.event.SecondTickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UpdateSectorTimer implements Listener {
    @EventHandler
    public void onTick(MinuteTickEvent event) {
        for (WorldSector sector : GlobalWorld.worldSectors.values()) {
            for (SectorLocation location : sector.zones()) {
                location.activity();
            }
        }
    }
}
