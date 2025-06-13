package net.annova.environment.world;

import net.annova.environment.world.sector0.SuruWahare;
import net.annova.thread.TaskMaster;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.HashMap;
import java.util.Map;

import static net.annova.main.AnnovaRPG.getInstance;

public class GlobalWorld {
    public static final Map<Integer, WorldSector> worldSectors = new HashMap<>() {{
        this.put(0, new SuruWahare());
    }};

    public static void init() {
        TaskMaster.syncTimer(getInstance(), () -> { // 5 seconds Movement Timer
            worldSectors.values().parallelStream().forEach(WorldSector::movement);
        }, 100);
    }

    public static World loadWorldAndGet(String worldName) {
        World world = Bukkit.getWorld(worldName);
        return world == null ? Bukkit.createWorld(new WorldCreator(worldName)) : world;
    }

    public static boolean isNight(World world) {
        long time = world.getTime();
        return time >= 12000;
    }
}
