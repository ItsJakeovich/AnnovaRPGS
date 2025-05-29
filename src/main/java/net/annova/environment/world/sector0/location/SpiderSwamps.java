package net.annova.environment.world.sector0.location;

import net.annova.environment.world.Location;
import org.bukkit.entity.Player;

import static net.annova.environment.world.sector0.SuruWahare.world0;

public class SpiderSwamps implements Location {
    public static final org.bukkit.Location outerSwamps = new org.bukkit.Location(world0, -756, 63, 155);
    public static final org.bukkit.Location innerSwamps = new org.bukkit.Location(world0, -727, 12, 241);

    @Override
    public void movement(Player player) {
        if (player.getLocation().distanceSquared(outerSwamps) < 576) {

        }
    }
}
