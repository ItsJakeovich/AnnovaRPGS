package net.annova.object.person.advancement.event;

import net.annova.environment.world.sector0.location.Oasis;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.advancement.RPGAdvancement;
import net.annova.thread.event.MinuteTickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.GlobalRPG.getRPGPersons;

public class MinuteEvent implements Listener {
    @EventHandler
    public void afk(MinuteTickEvent event) {
        for (RPGPerson person : getRPGPersons()) {
            if (person.getSectorLocation() instanceof Oasis) {
                if (person.getPerson().getMinutesInAFK() < 30) continue;
                person.completeAdvancement(RPGAdvancement.PACIFICATION);
            }
        }
    }
}
