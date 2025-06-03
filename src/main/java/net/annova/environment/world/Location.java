package net.annova.environment.world;

import net.annova.environment.world.advancement.RPGAdvancement;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Set;

import static net.annova.util.BukkitObjects.random;

public interface Location {
    void movement(Player player);

    Set<RPGAdvancement> advancements();

    org.bukkit.Location[] points();

    default boolean isOutside(int squaredRadius, org.bukkit.Location location, org.bukkit.Location... points) {
        boolean isOutside = true;
        for (org.bukkit.Location locationPoint : points) {
            if (locationPoint.distanceSquared(locationPoint) < squaredRadius) isOutside = false;
        }
        return isOutside;
    }

    static org.bukkit.Location getChorusLocation(org.bukkit.Location location, int radius) {
        for (int i = 0; i < radius*radius; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 2 * radius;
            double offsetY = (random.nextDouble() - 0.5) * 2 * radius / 2;
            double offsetZ = (random.nextDouble() - 0.5) * 2 * radius;

            org.bukkit.Location check = location.clone().add(new Vector(offsetX, offsetY, offsetZ));

            // Проверяем, что целевая позиция безопасна для телепортации
            if (!check.getBlock().getLocation().clone().add(0, -1, 0).getBlock().getType().isSolid()
                    || check.getBlock().getLocation().clone().add(0, 1, 0).getBlock().getType().isSolid()
                    || check.getBlock().getType().isSolid()) continue;

            return check;
        }
        return location;
    }
}
