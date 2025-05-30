package net.annova.event.custom;

import net.annova.entity.core.RPGEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;

public class RPGEntityDeathCE extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final RPGEntity entity;
    private final EntityDeathEvent event;

    public RPGEntityDeathCE(RPGEntity entity, EntityDeathEvent event) {
        this.entity = entity;
        this.event = event;
    }

    public RPGEntity getEntity() {
        return entity;
    }
    public EntityDeathEvent getEvent() {
        return event;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
