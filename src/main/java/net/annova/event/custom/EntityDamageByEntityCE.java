package net.annova.event.custom;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityCE extends EntityDamageCE {

    private final Entity damager;

    public EntityDamageByEntityCE(LivingEntity entity, EntityDamageByEntityEvent event) {
        super(entity, event);
        this.damager = event.getDamager();
    }

    public Entity getDamager() {
        return damager;
    }
}
