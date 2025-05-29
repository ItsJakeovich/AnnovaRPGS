package net.annova.event;

import net.annova.event.custom.PostPersonQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class QuitRPGEvent implements Listener {
    @EventHandler
    public void quit(PostPersonQuitEvent event) {
        if (event.getPlayer().isOp()) event.getEvent().setQuitMessage("");
    }
}
