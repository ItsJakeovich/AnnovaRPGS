package net.annova.event.custom;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class LivingEntityDamageByEntityCE extends MobDamageCE {

    private final Entity damager;

    public LivingEntityDamageByEntityCE(LivingEntity entity, EntityDamageByEntityEvent event) {
        super(entity, event);
        this.damager = event.getDamager();
    }

    public Entity getDamager() {
        return damager;
    }
}
