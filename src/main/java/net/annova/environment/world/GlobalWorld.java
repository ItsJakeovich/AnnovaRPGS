package net.annova.environment.world;

import net.annova.environment.world.sector0.SuruWahare;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.HashMap;
import java.util.Map;

import static net.annova.environment.world.sector0.SuruWahare.world0;

public class GlobalWorld {
    public static final Map<World, WorldSector> worldSectors = new HashMap<>() {{
        this.put(world0, new SuruWahare());
    }};

    public static World loadWorldAndGet(String worldName) {
        World world = Bukkit.getWorld(worldName);
        return world == null ? Bukkit.createWorld(new WorldCreator(worldName)) : world;
    }

    public static boolean isNight(World world) {
        long time = world.getTime();
        return time >= 12000;
    }
}
