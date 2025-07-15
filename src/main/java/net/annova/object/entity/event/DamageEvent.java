package net.annova.object.entity.event;

import net.annova.event.custom.MobDamageCE;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.ability.ValueType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static net.annova.environment.world.SectorLocation.getChorusLocation;
import static net.annova.main.GlobalRPG.getRPGEntity;
import static net.annova.util.BukkitObjects.chance;

public class DamageEvent implements Listener {
    @EventHandler
    public void onDamage(MobDamageCE event) {
        LivingEntity entity = event.getEntity();
        RPGEntity rpg = getRPGEntity(entity);
        if (rpg == null) return;

        for (RPGAbility ability : rpg.abilities()) {
            if (!chance(ability.getValue(ValueType.CHANCE))) continue;
            switch (ability.getType()) {
                case EXPLOSION_NEUTRALITY -> {
                    EntityDamageEvent.DamageCause cause = event.getEvent().getCause();
                    if (cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION
                            || cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) continue;
                    event.setCancelled(true);
                }
                case RESISTANCE -> event.increaseDamage(event.getBaseDamage()*ability.getValue(ValueType.MAIN));
                case AVOIDANCE -> {
                    entity.teleport(getChorusLocation(entity.getLocation(),
                            ability.getValue(ValueType.RADIUS).intValue())
                    );
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                            ability.getValue(ValueType.DURATION).intValue(), 0)
                    );
                }
                case IMMORTAL, SCHIVARE -> event.setCancelled(true);
            }
        }
    }
}
