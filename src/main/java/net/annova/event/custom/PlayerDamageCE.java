package net.annova.event.custom;

import net.minecraft.world.entity.player.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageCE extends MobDamageCE {
    private final Player player;

    public PlayerDamageCE(Player player, EntityDamageEvent event) {
        super(player, event);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
