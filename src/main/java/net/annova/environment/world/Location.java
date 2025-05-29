package net.annova.environment.world;

import org.bukkit.entity.Player;

public interface Location {
    void movement(Player player);

    default boolean isOutside(int squaredRadius, org.bukkit.Location location, org.bukkit.Location... points) {
        boolean isOutside = true;
        for (org.bukkit.Location locationPoint : points) {
            if (locationPoint.distanceSquared(locationPoint) < squaredRadius) isOutside = false;
        }
        return isOutside;
    }
}
