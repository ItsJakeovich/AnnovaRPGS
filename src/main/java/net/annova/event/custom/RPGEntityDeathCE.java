package net.annova.event.custom;

import net.annova.object.entity.core.RPGEntity;
import net.annova.object.person.RPGPerson;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;

import javax.annotation.Nullable;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class RPGEntityDeathCE extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final RPGEntity entity;
    private final RPGPerson killer;
    private final EntityDeathEvent event;

    public RPGEntityDeathCE(RPGEntity entity, Player killer, EntityDeathEvent event) {
        this.entity = entity;
        this.killer = getRPGPerson(killer);
        this.event = event;
    }

    public RPGEntity getEntity() {
        return entity;
    }

    @Nullable
    public RPGPerson getKiller() {
        return killer;
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
