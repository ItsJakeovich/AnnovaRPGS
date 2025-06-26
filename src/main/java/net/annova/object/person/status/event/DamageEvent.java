package net.annova.object.person.status.event;

import net.annova.event.custom.BattleCE;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.ValueType;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class DamageEvent implements Listener {
    @EventHandler
    public void status(BattleCE event) {
        if (event.getDamager() instanceof Player player) {
            RPGPerson person = getRPGPerson(player);
            double value = person.getFinalValue(ValueType.DAMAGE);
            if (value != 0) {
                event.increaseDamage(value * ((CraftPlayer) player).getHandle().getAttackStrengthScale(0.0F));
            }
            value = person.getFinalValue(ValueType.DAMAGE_MULTIPLIER);
            if (value != 0) {
                event.increaseDamage(event.getBaseDamage() * value);
            }
        }
    }
}
