package net.annova.object.entity.event;

import io.papermc.paper.event.entity.EntityKnockbackEvent;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.ability.ValueType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.config.AnnovaConfig.sendInfo;
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
                    event.setKnockback(event.getKnockback().multiply(1-resistance));
                }
            }
        }
    }
}
