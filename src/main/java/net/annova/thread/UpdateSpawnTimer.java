package net.annova.thread;

import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.WorldSector;
import net.annova.object.LocationCategory;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.RPGPerson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static net.annova.environment.world.GlobalWorld.worldSectors;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.entity.core.RPGEntityType.getEntitiesByCategory;
import static net.annova.util.BukkitObjects.random;

public class UpdateSpawnTimer extends AnnovaTask {
    @Override
    public void run() {
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
                entities[random.nextInt(entities.length)].tryNaturalSpawn(location);
            }
            player.playSound(player, "howlsothestorm", 5, 5);
        }
    }
}
