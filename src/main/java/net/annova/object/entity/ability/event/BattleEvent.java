package net.annova.object.entity.ability.event;

import net.annova.event.custom.BattleCE;
import net.annova.object.entity.core.AbilityType;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.ValueType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static net.annova.environment.world.Location.getChorusLocation;
import static net.annova.main.GlobalRPG.getEntity;
import static net.annova.object.entity.core.RPGEntity.addPotionEffect;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class BattleEvent implements Listener {
    @EventHandler
    public void onBattle(BattleCE event) {
        LivingEntity entity = event.getEntity();
        LivingEntity damager = event.getDamager();
        if (entity == damager) return;
        RPGEntity rpg = getEntity(entity);
        if (rpg == null) return;

        for (AbilityType ability : rpg.abilities()) {
            if (!chance(ability.getValue(ValueType.CHANCE))) continue;
            switch (ability) {
                case SPIKES -> {
                    double value = ability.getValue(ValueType.MAIN);
                    if (value >= 1) event.setCancelled(true);
                    damager.damage(event.getBaseDamage()*value, damager);
                }
            }
        }
    }

    @EventHandler
    public void abilities(BattleCE event) {
        this.entityAbilities(event);
        this.damagerAbilities(event);
    }

    private void entityAbilities(BattleCE event) {
        LivingEntity entity = event.getEntity();
        RPGEntity ent = getEntity(entity);
        if (ent == null) return;

        LivingEntity damager = event.getDamager();

        for (AbilityType ability : ent.abilities()) {
            if (!chance(ability.getValue("chance"))) continue;

            switch (ability) {
                case RESISTANCE -> event.setDamage(event.getDamage() - event.getBaseDamage()*ability.getValue("resist"));
                case KNOCKBACK_RESISTANCE -> entity.setVelocity(entity.getVelocity().multiply(1 - ability.getValue("resist")));
                case SCHIVARE -> event.setCancelled(true);
                case AVOIDANCE -> {
                    entity.teleport(getChorusLocation(entity.getLocation(), (int) ability.getValue("radius").doubleValue()));
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, (int) ability.getValue("duration").doubleValue(), 0));
                }
                case REFLECT -> {
                    event.setCancelled(true);
                    if (chance(0.75)) { // 75% шанс вернуть атаку отправителю
                        double damage = event.getDamage();
                        damager.damage(chance(0.25) ? damage*1.5 : damage, entity); // 25% шанс x1.5 урона
                    }
                }
            }
        }
    }
    private void damagerAbilities(BattleCE event) {
        LivingEntity damager = event.getDamager();
        RPGEntity ent = getEntity(damager);
        if (ent == null) return;

        LivingEntity entity = event.getEntity();

        for (AbilityType ability : ent.abilities()) {
            if (!chance(ability.getValue("chance"))) continue;

            switch (ability) {
                case LEVELLER -> addPotionEffect(entity, PotionEffectType.BLINDNESS, ability.getValue("duration"));
                case SLOWDOWN -> addPotionEffect(entity, PotionEffectType.SLOWNESS, ability.getValue("duration"));
                case VAMPIRISM -> ent.regen(ability.getValue("regen").floatValue());
                case VITIATION -> {
                    PotionEffectType type = null;
                    switch (random.nextInt(3)) {
                        case 0 -> type = PotionEffectType.BLINDNESS;
                        case 1 -> type = PotionEffectType.SLOWNESS;
                        case 2 -> type = PotionEffectType.NAUSEA;
                    }
                    addPotionEffect(entity, type, ability.getValue(STR."\{type.getKeyOrThrow().getKey()}_duration"));
                }
            }
        }
    }
}
