package net.annova.thread;

import net.annova.environment.world.SectorLocation;
import net.annova.object.LocationCategory;
import net.annova.object.entity.core.RPGEntity;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.RPGPerson;
import net.annova.threading.AnnovaAsyncTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.entity.core.RPGEntityType.getEntitiesByCategory;
import static net.annova.util.BukkitObjects.random;

public class UpdateSpawnTimer extends AnnovaAsyncTask {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            RPGPerson person = getRPGPerson(player);
            SectorLocation sectorLocation = person.getSectorLocation();
            List<RPGEntityType> entities;
            if (sectorLocation != null) entities = List.copyOf(sectorLocation.entitiesToSpawn());
            else                        entities = List.copyOf(getEntitiesByCategory(LocationCategory.EVERYWHERE));

            // Entity spawn
            Location location = player.getLocation();
            for (int i = random.nextInt(5)+1; i != 0; i--) { // от 1 до 5
                RPGEntityType type = entities.get(random.nextInt(entities.size()));
                if (type.isBoss() && sectorLocation.getBoss() != null) {
                    i++;
                    continue;
                }
                RPGEntity entity = type.tryNaturalSpawn(location);
                if (sectorLocation != null) {
                    if (type.isBoss()) sectorLocation.setBoss(entity);
                    entity.setSectorLocation(sectorLocation);
                }
            }
            player.playSound(player, "howlsothestorm", 5, 5);
        }
    }
}
