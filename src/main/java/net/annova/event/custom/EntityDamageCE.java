package net.annova.event.custom;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class EntityDamageCE extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final LivingEntity entity;
    private final EntityDamageEvent event;
    private double damage;
    private final double baseDamage;

    private boolean cancelled = false;

    public EntityDamageCE(LivingEntity entity, EntityDamageEvent event) {
        this.entity = entity;
        this.event = event;
        double damage = event.getDamage();
        this.damage = damage;
        this.baseDamage = damage;
    }

    public LivingEntity getEntity() {
        return entity;
    }
    public EntityDamageEvent getEvent() {
        return event;
    }

    public double getDamage() {
        return damage;
    }
    public void setDamage(double damage) {
        this.damage = damage;
    }
    public void increaseDamage(double damage) {
        this.damage += damage;
    }
    public void decreaseDamage(double damage) {
        this.damage -= damage;
    }

    public double getBaseDamage() {
        return baseDamage;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public boolean isPlayer(Entity entity) {
        return entity instanceof Player;
    }
}
