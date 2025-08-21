package net.annova.environment.world;

import net.annova.environment.world.sector0.SuruWahare;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.GlobalRPG.addChunkCollection;
import static net.annova.util.BukkitObjects.getWorld;

public class GlobalWorld {
    public static ServerLevel LOBBY = getWorld("lobby");

    public static final Map<ServerLevel, WorldSector> worldSectors = new HashMap<>();
    public static void initSectors() {
        worldSectors.put(world0, new SuruWahare());

        for (ServerLevel level : worldSectors.keySet()) {
            addChunkCollection(level);
        }
    }

    public static Map<ServerLevel, WorldSector> getWorldSectors() {
        return worldSectors;
    }
    public static WorldSector getWorldSector(ServerLevel level) {
        return worldSectors.get(level);
    }

    public static boolean isNight(World world) {
        long time = world.getTime();
        return time >= 12000;
    }
    public static boolean isNight(Level level) {
        long time = level.getDayTime() % 24000L;
        if (time < 0L) time += 24000L;
        return time >= 12000;
    }
}
