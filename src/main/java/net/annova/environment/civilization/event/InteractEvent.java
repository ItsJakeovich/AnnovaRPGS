package net.annova.environment.civilization.event;

import net.annova.event.custom.RightClickCE;
import net.annova.object.Person;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.item.base.ItemType.CAMP;

public class InteractEvent implements Listener {
    @EventHandler
    public void interact(RightClickCE event) {
        if (!CAMP.equals(event.getItemInHand())) return;

        Person person = event.getPerson();
        person.send(getInstance(), "camp-set-action");
        person.getConfirm().setConfirmType("set-camp");
    }
}
