package net.annova.event;

import net.annova.event.custom.PostPersonQuitEvent;
import net.annova.object.person.RPGPerson;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class QuitRPGEvent implements Listener {
    @EventHandler
    public void quit(PostPersonQuitEvent event) {
        Player player = event.getPlayer();
        RPGPerson person = getRPGPerson(player);
        person.setScene(false);

        if (event.getPlayer().isOp()) event.getEvent().setQuitMessage("");
    }
}
