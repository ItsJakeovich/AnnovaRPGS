package net.annova.thread;

import net.annova.environment.RPGSpawning;
import net.annova.threading.AnnovaAsyncTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.Set;

import static net.annova.event.AbilitiesEvent.getChorusLocation;
import static net.annova.util.BukkitObjects.chance;

public class UpdateSpawnTimer extends AnnovaAsyncTask {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (RPGSpawning spawning : RPGSpawning.values()) {
                if (!chance(spawning.getChance())) continue;
                Location location = player.getLocation();
                Set<Biome> biomes = spawning.getBiomes();
                if (!biomes.isEmpty()) if (!biomes.contains(location.getBlock().getBiome())) continue;
                World world = spawning.getWorld();
                World spawnWorld = location.getWorld();
                if (world != null) if (!world.equals(spawnWorld)) continue;
                World.Environment env = spawning.getEnvironment();
                if (env != null) if (!env.equals(spawnWorld.getEnvironment())) continue;

                Location spawn = getChorusLocation(location, 15);
                if (spawn.getBlock().getLightLevel() > 7) continue;
                spawning.getEntity().create(spawn);
            }


            //player.playSound(player, "howlsothestorm", 5, 5);
        }
    }
}
