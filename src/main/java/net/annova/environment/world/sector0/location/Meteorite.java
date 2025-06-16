package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.RPGAdvancement;
import net.annova.object.LocationCategory;
import net.annova.object.entity.core.RPGEntityType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Set;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.object.entity.core.RPGEntityType.getEntitiesByCategory;

public class Meteorite extends SectorLocation {
    private static final org.bukkit.Location theMeteorite = new org.bukkit.Location(world0, 730, 51, -191);

    @Override
    public void movement(Player player) {
        double pDistanceSquared = player.getLocation().distanceSquared(theMeteorite);
        if (pDistanceSquared <= this.squaredRadius()) {
            player.setFireTicks(120);
            if (player.getGameMode() == GameMode.SURVIVAL) {
                double damage;
                int fireTicks;
                if (pDistanceSquared <= 576) {
                    damage = 16;
                    fireTicks = 600;
                }
                else {
                    double koefficient = 1 - pDistanceSquared/3600;
                    damage = 6 * koefficient;
                    fireTicks = (int) (200 * koefficient);
                }
                player.setFireTicks(fireTicks);
                player.damage(damage);
            }
        }
    }

    @Override
    public Set<RPGAdvancement> advancements() {
        return Set.of();
    }

    @Override
    public Set<RPGEntityType> entitiesToSpawn() {
        return getEntitiesByCategory(LocationCategory.S0_METEORITE);
    }

    @Override
    public org.bukkit.Location[] points() {
        return new org.bukkit.Location[] {
                new org.bukkit.Location(world0, 715, 70, -183)
        };
    }

    @Override
    public int squaredRadius() {
        return 3600;
    }
}