package net.annova.thread;

import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.WorldSector;
import net.annova.object.LocationCategory;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.RPGPerson;
import net.annova.thread.event.MinuteTickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.environment.world.GlobalWorld.worldSectors;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.entity.core.RPGEntityType.getEntitiesByCategory;
import static net.annova.util.BukkitObjects.random;

public class UpdateSpawnTimer implements Listener {
    @EventHandler
    public void run(MinuteTickEvent event) {
        for (WorldSector sector : worldSectors.values()) {
            for (SectorLocation location : sector.zones()) {
                Location[] points = location.points();
                for (RPGEntityType entity : location.entitiesToSpawn()) {
                    entity.tryNaturalSpawn(points[random.nextInt(points.length)]);
                }
            }
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            RPGPerson person = getRPGPerson(player);
            if (person.getSectorLocation() != null) continue;

            RPGEntityType[] entities = getEntitiesByCategory(LocationCategory.EVERYWHERE).toArray(new RPGEntityType[0]);

            // Entity spawn
            Location location = player.getLocation();
            for (int i = random.nextInt(5)+1; i != 0; i--) { // от 1 до 5
                RPGEntityType entity = entities[random.nextInt(entities.length)];
                if (entity.getCondition().canSpawn(location)) entity.tryNaturalSpawn(location);
            }
        }
    }
}
