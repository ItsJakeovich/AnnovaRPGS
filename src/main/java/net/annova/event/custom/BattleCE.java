package net.annova.event.custom;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import static net.annova.main.Keys.NK_ID;


public class BattleCE extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final EntityDamageByEntityEvent event;

    public BattleCE(EntityDamageByEntityEvent event) {
        this.event = event;
    }

    public EntityDamageByEntityEvent getEvent() {
        return event;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isTrueEntity(Entity entity) {
        return entity.getPersistentDataContainer().has(NK_ID, PersistentDataType.INTEGER)
                || entity instanceof Player;
    }
    public int getKey(Entity entity) {
        if (entity == null) return -1;
        return entity.getPersistentDataContainer().get(NK_ID, PersistentDataType.INTEGER);
    }
}
