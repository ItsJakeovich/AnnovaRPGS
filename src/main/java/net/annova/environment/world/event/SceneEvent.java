package net.annova.environment.world.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class SceneEvent implements Listener {
    @EventHandler
    public void command(PlayerCommandPreprocessEvent event) {
        if (getRPGPerson(event.getPlayer()).isScene()) event.setCancelled(true);
    }
}
