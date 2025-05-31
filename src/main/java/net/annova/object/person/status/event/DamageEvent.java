package net.annova.object.person.status.event;

import net.annova.event.custom.BattleCE;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.ValueType.DAMAGE_BUFF;
import static net.annova.object.person.status.ValueType.DAMAGE_DEBUFF;

public class DamageEvent implements Listener {
    @EventHandler
    public void status(BattleCE event) {
        if (event.getDamager() instanceof Player player) {
            RPGPerson person = getRPGPerson(player);
            StatusManager manager = person.getStatusManager();
            double value = manager.getFinalValue(DAMAGE_BUFF)
                    - manager.getFinalValue(DAMAGE_DEBUFF);
            if (value == 0) return;
            event.increaseDamage(event.getBaseDamage() * value);
        }
    }
}
