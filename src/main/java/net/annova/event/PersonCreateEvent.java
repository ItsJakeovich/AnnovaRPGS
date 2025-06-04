package net.annova.event;

import net.annova.event.custom.CreatePersonCE;
import net.annova.object.Person;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.GlobalRPG.addRPGPerson;
import static net.annova.main.GlobalRPG.containsRPGPerson;

public class PersonCreateEvent implements Listener {
    @EventHandler
    public void login(CreatePersonCE event) {
        Person person = event.getPerson();
        if (containsRPGPerson(person.getName())) return;
        addRPGPerson(person);
    }
}
