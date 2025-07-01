package net.annova.object.person.status.event;

import net.annova.event.custom.MobDamageCE;
import net.annova.object.person.RPGPerson;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.ValueType.*;

public class DefenceEvent implements Listener {
    @EventHandler
    public void status(MobDamageCE event) {
        if (event.getEntity() instanceof Player player) {
            RPGPerson person = getRPGPerson(player);
            // снижает получаемый урон относительно уровня защиты.
            event.decreaseDamage(person.getFinalValue(DEFENCE) + event.getBaseDamage() * person.getFinalValue(DEFENCE_MULTIPLIER));

            if (event.getEvent().getCause() == EntityDamageEvent.DamageCause.FALL) {
                // FALL_MULTIPLIER - модификатор получения урона от падения
                event.increaseDamage(event.getBaseDamage() * person.getFinalValue(FALL_MULTIPLIER));
            }
        }
    }
}
