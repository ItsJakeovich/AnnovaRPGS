package net.annova.event.custom;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BattleCE extends EntityDamageByEntityCE {
    private static final HandlerList handlers = new HandlerList();

    private final LivingEntity damager;

    public BattleCE(LivingEntity entity, LivingEntity damager, EntityDamageByEntityEvent event) {
        super(entity, event);
        this.damager = damager;
    }

    public LivingEntity getDamager() {
        return damager;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
