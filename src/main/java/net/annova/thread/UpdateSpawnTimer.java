package net.annova.thread;

import net.annova.environment.world.SectorLocation;
import net.annova.object.LocationCategory;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.RPGPerson;
import net.annova.threading.AnnovaAsyncTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
            Set<RPGEntityType> entities;
            if (sectorLocation != null) entities = sectorLocation.entitiesToSpawn();
            else                        entities = getEntitiesByCategory(LocationCategory.EVERYWHERE);

            // Entity spawn
            Location location = player.getLocation();
            int i = random.nextInt(5)+1;
            for (RPGEntityType entity : entities) {
                if (i == 0) break;
                if (entity.tryNaturalSpawn(location)) i--;
            }
            player.playSound(player, "howlsothestorm", 5, 5);
        }
    }
}
