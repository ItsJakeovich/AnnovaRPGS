package net.annova.environment.world;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Set;

import static net.annova.util.BukkitObjects.random;

public interface Location {
    void movement(Player player);

    Set<RPGAdvancement> advancements();

    org.bukkit.Location[] points();
    int squaredRadius();

    default boolean isOutside(org.bukkit.Location location) {
        for (org.bukkit.Location locationPoint : this.points()) {
            if (locationPoint.distanceSquared(locationPoint) < this.squaredRadius()) return false;
        }
        return true;
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

    default void showTerritory(World world) {
        org.bukkit.Location check = this.points()[0].clone();
        check.setY(300);
        Material action = check.getBlock().getType().isAir() ? Material.REDSTONE_BLOCK : Material.AIR;
        for (org.bukkit.Location location : this.points()) {
            int radius = (int) Math.sqrt(this.squaredRadius());
            for (int z = -radius; z <= radius; z++) {
                for (int x = -radius; x <= radius; x++) {
                    new org.bukkit.Location(world, x, 300, z).getBlock().setType(action);
                }
            }
        }
    }
}
