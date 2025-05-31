package net.annova.event.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageCE extends EntityDamageCE {
    private final Player player;

    public PlayerDamageCE(Player player, EntityDamageEvent event) {
        super(player, event);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
