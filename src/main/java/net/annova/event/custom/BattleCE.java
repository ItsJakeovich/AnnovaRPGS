package net.annova.event.custom;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class BattleCE extends EntityDamageByEntityCE {
    private final LivingEntity damager;

    public BattleCE(LivingEntity entity, LivingEntity damager, EntityDamageByEntityEvent event) {
        super(entity, event);
        this.damager = damager;
    }

    public LivingEntity getDamager() {
        return damager;
    }
}
