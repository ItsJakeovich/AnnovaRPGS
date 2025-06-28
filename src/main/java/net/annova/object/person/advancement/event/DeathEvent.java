package net.annova.object.person.advancement.event;

import net.annova.event.custom.RPGEntityDeathCE;
import net.annova.object.entity.mobs.sector0.oasis.Crab;
import net.annova.object.person.RPGPerson;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.object.person.advancement.RPGAdvancement.GIVE_ME_CRAB;

public class DeathEvent implements Listener {
    @EventHandler
    public void death(RPGEntityDeathCE event) {
        RPGPerson person = event.getKiller();
        if (person == null) return;
        if (event.getEntity() instanceof Crab) {
            person.completeAdvancement(GIVE_ME_CRAB);
        }
    }
}
