package net.annova.object.entity.event;

import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.ability.ValueType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityKnockbackEvent;
import org.bukkit.util.Vector;

import static net.annova.main.GlobalRPG.getEntity;
import static net.annova.util.BukkitObjects.chance;

public class KnockbackEvent implements Listener {
    @EventHandler
    public void onDamage(EntityKnockbackEvent event) {
        RPGEntity entity = getEntity(event.getEntity());
        if (entity == null) return;

        for (RPGAbility ability : entity.abilities()) {
            if (!chance(ability.getValue(ValueType.CHANCE))) continue;
            switch (ability.getType()) {
                case KNOCKBACK_RESISTANCE -> {
                    double resistance = ability.getValue(ValueType.MAIN);
                    if (resistance >= 1) {
                        event.setCancelled(true);
                        return;
                    }
                    Vector vector = event.getFinalKnockback();
                    event.setFinalKnockback(vector.multiply(1-resistance));
                }
            }
        }
    }
}
