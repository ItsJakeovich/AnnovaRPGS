package net.annova.event;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingRPGEvent implements Listener {
    @EventHandler
    public void fish(PlayerFishEvent event) {
        if (event.getCaught() instanceof Item item) {

        }
    }
}
