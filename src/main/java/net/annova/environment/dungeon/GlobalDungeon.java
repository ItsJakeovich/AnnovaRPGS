package net.annova.environment.dungeon;

import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.portal.TeleportTransition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static net.annova.util.BukkitObjects.getWorld;

public class GlobalDungeon {
    private static final ServerLevel dungeonWorld = getWorld("dungeon_world");
    public static ServerLevel getDungeonWorld() {
        return dungeonWorld;
    }

    private static final Map<Integer, Dungeon> dungeons = new HashMap<>();
    private static final Map<ServerLevel, Map<Vec3i, Dungeon>> transitionMap = new HashMap<>();
    static { transitionMap.put(getDungeonWorld(), new HashMap<>()); }
    public static Dungeon getDungeon(int key) {
        return dungeons.get(key);
    }
    public static void addDungeon(int key, Dungeon dungeon) {
        dungeons.put(key, dungeon);

        transitionMap.putIfAbsent(dungeon.getSummonLevel(), new HashMap<>());
        transitionMap.get(dungeon.getLevel()).put(dungeon.getVec3iSpawnPosition(), dungeon);
        transitionMap.get(dungeon.getSummonLevel()).put(dungeon.getSummonPosition(), dungeon);
    }
    public static void removeDungeon(Dungeon dungeon) {
        dungeons.remove(dungeon.getKey());
        
        transitionMap.get(dungeon.getLevel()).remove(dungeon.getVec3iSpawnPosition());
        transitionMap.get(dungeon.getSummonLevel()).remove(dungeon.getSummonPosition());
    }
    public static Collection<Dungeon> getDungeons() {
        return dungeons.values();
    }
    public static Dungeon getDungeon(ServerLevel level, Vec3i vec3i) {
        Map<Vec3i, Dungeon> transitions = transitionMap.get(level);
        if (transitions == null) return null;
        for (Vec3i vec : transitions.keySet()) {
            if (vec.distToCenterSqr(vec3i.getX(), vec3i.getY(), vec3i.getZ()) < 7) return transitions.get(vec);
        }
        return null;
    }
}
