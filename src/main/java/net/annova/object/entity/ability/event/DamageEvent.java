package net.annova.object.entity.ability.event;

import net.annova.event.custom.EntityDamageCE;
import net.annova.object.entity.core.AbilityType;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.ValueType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import static net.annova.main.GlobalRPG.getEntity;
import static net.annova.util.BukkitObjects.chance;

public class DamageEvent implements Listener {
    @EventHandler
    public void onDamage(EntityDamageCE event) {
        LivingEntity entity = event.getEntity();
        RPGEntity rpg = getEntity(entity);
        if (rpg == null) return;

        for (AbilityType ability : rpg.abilities()) {
            if (!chance(ability.getValue(ValueType.CHANCE))) continue;
            switch (ability) {
                case EXPLOSION_NEUTRALITY -> {
                    EntityDamageEvent.DamageCause cause = event.getEvent().getCause();
                    if (cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION
                            || cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) continue;
                    event.setCancelled(true);
                }
                case IMMORTAL -> event.setCancelled(true);
            }
        }
    }
}
