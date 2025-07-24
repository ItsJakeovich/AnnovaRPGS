package net.annova.object.entity.event;

import net.annova.event.custom.BattleCE;
import net.annova.event.custom.LivingEntityDamageByEntityCE;
import net.annova.object.entity.core.attribute.EntityAction;
import net.annova.object.entity.core.attribute.ability.RPGAbility;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.attribute.ability.ValueType;
import net.annova.object.entity.core.status.EntityStatus;
import net.annova.object.entity.core.status.StatusType;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.GlobalRPG.getRPGEntity;
import static net.annova.object.entity.core.RPGEntity.hurtEntity;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;
import static net.minecraft.world.effect.MobEffects.*;

public class BattleEvent implements Listener {
    @EventHandler
    public void onArrow(LivingEntityDamageByEntityCE event) {
        if (event.getDamager() instanceof Arrow arrow
                && arrow.getShooter() instanceof org.bukkit.entity.LivingEntity living) {
            RPGEntity entity = getRPGEntity(living);
            if (entity == null) return;
            event.setDamage(entity.damage());
        }
    }
    @EventHandler
    public void onBattle(BattleCE event) {
        LivingEntity entity = event.getEntity();
        LivingEntity damager = event.getDamager();
        if (entity == damager) return;
        RPGEntity rpg = getRPGEntity(entity);
        if (rpg != null) { // при получении урона
            if (rpg instanceof EntityAction action) {
                action.onDamage(event, damager);
            }
            for (RPGAbility ability : rpg.abilities()) {
                if (!chance(ability.getValue(ValueType.CHANCE))) continue;
                switch (ability.getType()) {
                    case SPIKES -> {
                        double value = ability.getValue(ValueType.MAIN);
                        if (value >= 1) event.setCancelled(true);
                        hurtEntity(event.getBaseDamage() * value, damager, damager);
                    }
                    case REFLECT -> {
                        event.setCancelled(true);
                        if (chance(0.75)) { // 75% шанс вернуть атаку отправителю
                            double damage = event.getBaseDamage();
                            hurtEntity(chance(0.25) ? damage*1.5 : damage, damager, entity);
                        }
                    }
                }
            }
        }
        rpg = getRPGEntity(damager);
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
                    case SLOWDOWN -> entity.addEffect(new MobEffectInstance(SLOWNESS,
                            ability.getValue(ValueType.DURATION).intValue(),
                            ability.getValue(ValueType.LEVEL).intValue())
                    );
                    case VITIATION -> {
                        Holder<MobEffect> type = null;
                        switch (random.nextInt(3)) {
                            case 0 -> type = MobEffects.BLINDNESS;
                            case 1 -> type = MobEffects.SLOWNESS;
                            case 2 -> type = MobEffects.NAUSEA;
                        }
                        entity.addEffect(new MobEffectInstance(type, ability.getValue(ValueType.DURATION).intValue(), 0));
                    }
                    case LEVELLER ->
                            entity.addEffect(new MobEffectInstance(BLINDNESS, ability.getValue(ValueType.DURATION).intValue(), 0));
                    case POISON -> {
                        int duration = ability.getValue(ValueType.DURATION).intValue();
                        entity.addEffect(new MobEffectInstance(POISON, duration, 1));
                        entity.addEffect(new MobEffectInstance(SLOWNESS, duration, 0));
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
