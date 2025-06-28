package net.annova.environment.world;

import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.advancement.RPGAdvancement;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.EnumSet;
import java.util.Set;

import static net.annova.config.AnnovaConfig.sendInfo;
import static net.annova.util.BukkitObjects.random;

public abstract class SectorLocation {
    public abstract void activity();
    public abstract void movement(Player player);

    public abstract EnumSet<RPGEntityType> entitiesToSpawn();
    public abstract Location[] points();
    public abstract int squaredRadius();

    public boolean isOutside(Location location) {
        for (Location locationPoint : this.points()) {
            if (location.distanceSquared(locationPoint) <= this.squaredRadius()) return false;
        }
        return true;
    }

    private RPGEntity boss;

    public RPGEntity getBoss() {
        return boss;
    }
    public void setBoss(RPGEntity boss) {
        this.boss = boss;
    }

    public static Location getChorusLocation(Location location, int radius) {
        for (int i = 0; i < radius*radius; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 2 * radius;
            double offsetY = (random.nextDouble() - 0.5) * radius;
            double offsetZ = (random.nextDouble() - 0.5) * 2 * radius;

            Location check = location.clone().add(new Vector(offsetX, offsetY, offsetZ));

            // Проверяем, что целевая позиция безопасна для телепортации
            if (!check.getBlock().getLocation().clone().add(0, -1, 0).getBlock().getType().isSolid()
                    || check.getBlock().getLocation().clone().add(0, 1, 0).getBlock().getType().isSolid()
                    || check.getBlock().getType().isSolid()) continue;

            return check;
        }
        return location;
    }

    public void showTerritory(World world) {
        Location check = this.points()[0].clone();
        check.setY(300);
        Material action = check.getBlock().getType().isAir() ? Material.REDSTONE_BLOCK : Material.AIR;
        for (Location location : this.points()) {
            int lX = location.getBlockX(), lZ = location.getBlockZ();
            sendInfo(lX + ", 300, " + lZ);
            int radius = (int) Math.sqrt(this.squaredRadius());
            for (int z = -radius; z <= radius; z++) {
                for (int x = -radius; x <= radius; x++) {
                    Block block = new Location(world, lX+x, 300, lZ+z).getBlock();
                    if (!block.getType().equals(Material.REDSTONE_BLOCK) && !block.getType().equals(Material.AIR)) continue;
                    block.setType(action);
                }
            }
        }
    }
}
