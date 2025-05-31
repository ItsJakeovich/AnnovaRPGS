package net.annova.thread;

import net.annova.object.entity.core.RPGSpawning;
import net.annova.threading.AnnovaAsyncTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.Set;

import static net.annova.environment.world.Location.getChorusLocation;
import static net.annova.util.BukkitObjects.chance;

public class UpdateSpawnTimer extends AnnovaAsyncTask {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location location = player.getLocation();
            for (RPGSpawning spawning : RPGSpawning.values()) {
                spawning.trySpawn(location);
            }
            //player.playSound(player, "howlsothestorm", 5, 5);
        }
    }
}
