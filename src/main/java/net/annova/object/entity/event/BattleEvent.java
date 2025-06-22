package net.annova.object.entity.event;

import net.annova.event.custom.BattleCE;
import net.annova.event.custom.LivingEntityDamageByEntityCE;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.ability.ValueType;
import net.annova.object.entity.core.status.EntityStatus;
import net.annova.object.entity.core.status.StatusType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;

import static net.annova.main.GlobalRPG.getEntity;
import static net.annova.object.entity.core.RPGEntity.addPotionEffect;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class BattleEvent implements Listener {
    @EventHandler
    public void onArrow(LivingEntityDamageByEntityCE event) {
        if (event.getDamager() instanceof Arrow arrow
                && arrow.getShooter() instanceof LivingEntity living) {
            RPGEntity entity = getEntity(living);
            event.setDamage(entity.damage());
        }
    }
    @EventHandler
    public void onBattle(BattleCE event) {
        LivingEntity entity = event.getEntity();
        LivingEntity damager = event.getDamager();
        if (entity == damager) return;
        RPGEntity rpg = getEntity(entity);
        if (rpg != null) { // при получении урона
            if (rpg instanceof EntityAction action) {
                action.onDamage(damager);
            }
            for (RPGAbility ability : rpg.abilities()) {
                if (!chance(ability.getValue(ValueType.CHANCE))) continue;
                switch (ability.getType()) {
                    case SPIKES -> {
                        double value = ability.getValue(ValueType.MAIN);
                        if (value >= 1) event.setCancelled(true);
                        damager.damage(event.getBaseDamage() * value, damager);
                    }
                    case REFLECT -> {
                        event.setCancelled(true);
                        if (chance(0.75)) { // 75% шанс вернуть атаку отправителю
                            double damage = event.getBaseDamage();
                            damager.damage(chance(0.25) ? damage * 1.5 : damage, entity); // 25% шанс x1.5 урона
                        }
                    }
                }
            }
        }
        rpg = getEntity(damager);
        if (rpg != null) { // при нанесении урона
            if (rpg instanceof EntityAction action) {
                if (!action.onAttack(entity)) {
                    event.setCancelled(true);
                    return;
                }
            }
            for (RPGAbility ability : rpg.abilities()) {
                if (!chance(ability.getValue(ValueType.CHANCE))) continue;
                switch (ability.getType()) {
                    case VAMPIRISM -> rpg.regen((float) (event.getBaseDamage() * ability.getValue(ValueType.MAIN)));
                    case SLOWDOWN -> addPotionEffect(entity, PotionEffectType.SLOWNESS
                            , ability.getValue(ValueType.DURATION).intValue()
                            , ability.getValue(ValueType.LEVEL).intValue()
                    );
                    case VITIATION -> {
                        PotionEffectType type = null;
                        switch (random.nextInt(3)) {
                            case 0 -> type = PotionEffectType.BLINDNESS;
                            case 1 -> type = PotionEffectType.SLOWNESS;
                            case 2 -> type = PotionEffectType.NAUSEA;
                        }
                        addPotionEffect(entity, type, ability.getValue(ValueType.DURATION).intValue());
                    }
                    case LEVELLER ->
                            addPotionEffect(entity, PotionEffectType.BLINDNESS, ability.getValue(ValueType.DURATION).intValue());
                    case POISON -> {
                        int duration = ability.getValue(ValueType.DURATION).intValue();
                        addPotionEffect(entity, PotionEffectType.POISON, duration, 1);
                        addPotionEffect(entity, PotionEffectType.SLOWNESS, duration, 0);
                    }
                }
            }
            EntityStatus status = rpg.getStatus(StatusType.LEADER_DAMAGE_BUFF);
            if (status != null) {
                event.increaseDamage(event.getBaseDamage() * status.getType().getValue(net.annova.object.person.status.ValueType.ORATOR_DAMAGE_BUFF));
            }
        }
    }
}
