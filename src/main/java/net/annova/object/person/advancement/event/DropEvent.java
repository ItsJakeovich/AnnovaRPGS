package net.annova.object.person.advancement.event;

import net.annova.event.FishSwamAwayCE;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.advancement.RPGAdvancement.FRIENDS_ON_SCALES;

public class DropEvent implements Listener {
    @EventHandler
    public void fishDrop(FishSwamAwayCE event) {
        getRPGPerson(event.getPlayer()).completeAdvancement(FRIENDS_ON_SCALES);
    }
}
