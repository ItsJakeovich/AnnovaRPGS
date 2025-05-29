package net.annova.event;

import net.annova.event.custom.PersonHeightChanceCE;
import net.annova.object.Person;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class StaminaEvent implements Listener {
    @EventHandler
    public void jump(PersonHeightChanceCE event) {
        Person person = event.getPerson();

        if (event.isJump()) {
            if (person.getStamina() < 0.3f) event.getEvent().getTo().setY(event.getEvent().getFrom().getY());
            person.decreaseStamina(0.05f);
        }
    }
}
