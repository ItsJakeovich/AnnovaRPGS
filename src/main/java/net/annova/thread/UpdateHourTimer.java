package net.annova.thread;

import net.annova.main.AnnovaRPG;
import net.annova.thread.event.HourTickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UpdateHourTimer implements Listener {
    @EventHandler
    public void onTick(HourTickEvent event) {
        AnnovaRPG.saveAll();
    }
}
