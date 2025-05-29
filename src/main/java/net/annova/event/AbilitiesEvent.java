package net.annova.event;

import net.annova.entity.traits.ability.Ability;
import net.annova.entity.core.RPGEntity;
import net.annova.entity.traits.status.Status;
import net.annova.event.custom.BattleCE;
import net.annova.object.person.RPGPerson;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import static net.annova.environment.dungeon.Loot.random;
import static net.annova.main.GlobalRPG.getEntity;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.chance;

public class AbilitiesEvent implements Listener {
    @EventHandler
    public void buffs(BattleCE event) {
        EntityDamageByEntityEvent ev = event.getEvent();
        if (ev.getEntity() instanceof Player player) {
            RPGPerson person = getRPGPerson(player);
            Status status = person.getStatus(Status.Type.DEFENCE_DEBUFF);
            if (status != null) ev.setDamage(ev.getDamage() + ev.getDamage()*status.getValue("debuff"));
        }
        if (ev.getDamager() instanceof Player player) {
            RPGPerson person = getRPGPerson(player);
            Status status = person.getStatus(Status.Type.DAMAGE_DEBUFF);
            if (status != null) ev.setDamage(ev.getDamage() - ev.getFinalDamage()*status.getValue("debuff"));
        }
    }

    @EventHandler
    public void entity(BattleCE event) {
        EntityDamageByEntityEvent ev = event.getEvent();
        RPGEntity ent = getEntity(ev.getEntity());
        if (ent == null) return;

        LivingEntity entity = (LivingEntity) ev.getEntity();
        LivingEntity damager = (LivingEntity) ev.getDamager();

        for (Ability ability : ent.abilities()) {
            if (!chance(ability.getValue("chance"))) continue;

            switch (ability) {
                case RESISTANCE -> ev.setDamage(ev.getDamage() - ev.getFinalDamage()*ability.getValue("resist"));
                case KNOCKBACK_RESISTANCE -> entity.setVelocity(entity.getVelocity().multiply(1 - ability.getValue("resist")));
                case SCHIVARE -> ev.setCancelled(true);
                case AVOIDANCE -> {
                    entity.teleport(getChorusLocation(entity.getLocation(), (int) ability.getValue("radius").doubleValue()));
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, (int) ability.getValue("duration").doubleValue(), 0));
                }
                case REFLECT -> {
                    ev.setCancelled(true);
                    if (chance(0.75)) { // 75% шанс вернуть атаку отправителю
                        double damage = ev.getDamage();
                        damager.damage(chance(0.25) ? damage*1.5 : damage, entity); // 25% шанс x1.5 урона
                    }
                }
            }
        }
    }
    @EventHandler
    public void damager(BattleCE event) {
        EntityDamageByEntityEvent ev = event.getEvent();
        RPGEntity ent = getEntity(ev.getDamager());
        if (ent == null) return;

        LivingEntity entity = (LivingEntity) ev.getEntity();
        LivingEntity damager = (LivingEntity) ev.getDamager();

        for (Ability ability : ent.abilities()) {
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

    public static Location getChorusLocation(Location location, int radius) {
        for (int i = 0; i < radius*radius; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 2 * radius;
            double offsetY = (random.nextDouble() - 0.5) * 2 * radius / 2;
            double offsetZ = (random.nextDouble() - 0.5) * 2 * radius;

            Location check = location.clone().add(new Vector(offsetX, offsetY, offsetZ));

            // Проверяем, что целевая позиция безопасна для телепортации
            if (!check.getBlock().getLocation().clone().add(0, -1, 0).getBlock().getType().isSolid()
                    || check.getBlock().getLocation().clone().add(0, 1, 0).getBlock().getType().isSolid()
                    || check.getBlock().getType().isSolid()) continue;

            return check;
        }
        return location;
    }

    public static void addPotionEffect(LivingEntity entity, PotionEffectType type, double duration) {
        entity.addPotionEffect(new PotionEffect(type, (int) duration, 0));
    }
}
