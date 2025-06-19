package net.annova.event.custom;

import net.annova.object.person.RPGPerson;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class MouseClickCE extends Event {
    private static final HandlerList handlers = new HandlerList();

    public enum ClickType {
        RIGHT_CLICK,
        LEFT_CLICK
    }

    private final Player player;
    private final PlayerInteractEvent event;
    private final RPGPerson person;
    private final ClickType clickType;

    public MouseClickCE(Player player, PlayerInteractEvent event, ClickType clickType) {
        this.player = player;
        this.event = event;
        this.person = getRPGPerson(player);
        this.clickType = clickType;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerInteractEvent getEvent() {
        return event;
    }

    public RPGPerson getPerson() {
        return person;
    }

    public ClickType getClickType() {
        return clickType;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
