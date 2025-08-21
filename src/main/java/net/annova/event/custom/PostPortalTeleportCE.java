package net.annova.event.custom;

import net.annova.environment.world.RPGPortal;
import net.annova.object.person.RPGPerson;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PostPortalTeleportCE extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final RPGPortal portal;
    private final RPGPerson person;
    private final ServerPlayer player;

    public PostPortalTeleportCE(RPGPortal portal, RPGPerson person, ServerPlayer player) {
        this.portal = portal;
        this.person = person;
        this.player = player;
    }

    public RPGPortal getPortal() {
        return portal;
    }

    public RPGPerson getPerson() {
        return person;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
