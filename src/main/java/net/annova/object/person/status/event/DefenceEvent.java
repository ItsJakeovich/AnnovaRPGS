package net.annova.object.person.status.event;

import net.annova.event.custom.EntityDamageCE;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.ValueType.*;

public class DefenceEvent implements Listener {
    @EventHandler
    public void status(EntityDamageCE event) {
        if (event.getEntity() instanceof Player player) {
            RPGPerson person = getRPGPerson(player);
            double value = person.getFinalValue(DEFENCE);
            if (value == 0) return;
            event.decreaseDamage(event.getBaseDamage() * value);
        }
    }
}
