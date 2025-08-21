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
    private static final Map<Vec3i, TeleportTransition> transitionMap = new HashMap<>();
    public static Dungeon getDungeon(int key) {
        return dungeons.get(key);
    }
    public static void addDungeon(int key, Dungeon dungeon) {
        dungeons.put(key, dungeon);

        transitionMap.put(dungeon.getVec3iSpawnPosition(), dungeon.getSpawnTransition());
        transitionMap.put(dungeon.getSummonPosition(), dungeon.getSummonTransition());
    }
    public static void removeDungeon(Dungeon dungeon) {
        dungeons.remove(dungeon.getKey());

        transitionMap.remove(dungeon.getVec3iSpawnPosition());
        transitionMap.remove(dungeon.getSummonPosition());
    }
    public static Collection<Dungeon> getDungeons() {
        return dungeons.values();
    }
    public static TeleportTransition getPortalTransition(Vec3i vec3i) {
        return transitionMap.get(vec3i);
    }
}
