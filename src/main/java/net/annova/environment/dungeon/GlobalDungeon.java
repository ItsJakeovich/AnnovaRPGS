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
    private static final Map<ServerLevel, Map<Vec3i, TeleportTransition>> transitionMap = new HashMap<>();
    public static Dungeon getDungeon(int key) {
        return dungeons.get(key);
    }
    public static void addDungeon(int key, Dungeon dungeon) {
        dungeons.put(key, dungeon);

        ServerLevel level = dungeon.getLevel();
        transitionMap.putIfAbsent(level, new HashMap<>());
        transitionMap.get(level).put(dungeon.getVec3iSpawnPosition(), dungeon.getSpawnTransition());
        transitionMap.get(level).put(dungeon.getSummonPosition(), dungeon.getSummonTransition());
    }
    public static void removeDungeon(Dungeon dungeon) {
        dungeons.remove(dungeon.getKey());

        ServerLevel level = dungeon.getLevel();
        transitionMap.get(level).remove(dungeon.getVec3iSpawnPosition());
        transitionMap.get(level).remove(dungeon.getSummonPosition());
    }
    public static Collection<Dungeon> getDungeons() {
        return dungeons.values();
    }
    public static TeleportTransition getPortalTransition(ServerLevel level, Vec3i vec3i) {
        Map<Vec3i, TeleportTransition> transitions = transitionMap.get(level);
        if (transitions == null) return null;
        for (Vec3i vec : transitions.keySet()) {
            if (vec.distSqr(vec3i) < 7) return transitions.get(vec);
        }
        return null;
    }
}
