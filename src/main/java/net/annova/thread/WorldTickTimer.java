package net.annova.thread;

import net.annova.command.cmdRPG.RPGCode;
import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.world.RPGPortal;
import net.annova.object.RPGRarity;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import static net.annova.environment.dungeon.GlobalDungeon.getDungeonWorld;
import static net.annova.environment.dungeon.GlobalDungeon.getDungeons;
import static net.annova.object.RPGRarity.*;
import static net.annova.object.RPGRarity.COMMON;

public class WorldTickTimer extends AnnovaTask {
    @Override
    public void run() {
        WorldTickTimer.startPortalTick();
    }

    public static void startPortalTick() {
        // World portals
        RPGPortal portal = RPGPortal.RPG_ENTER;
        spawnPortalParticles(RPGCode.ALLOW_TO_JOIN ? UNCOMMON : MYTHIC, false,
                portal.getLevel(), portal.getX(), portal.getY(), portal.getZ()
        );
        // Dungeon portals
        ServerLevel dungeonLevel = getDungeonWorld();
        for (Dungeon dungeon : getDungeons()) {
            Vec3 vec3Spawn = dungeon.getSpawnPosition();
            Vec3i summon = dungeon.getSummonPosition();
            spawnPortalParticles(COMMON, true, dungeonLevel, Mth.floor(vec3Spawn.x), Mth.floor(vec3Spawn.y), Mth.floor(vec3Spawn.z));
            spawnPortalParticles(COMMON, true, dungeon.getSummonLevel(), summon.getX(), summon.getY(), summon.getZ());
        }
    }
    private static void spawnPortalParticles(RPGRarity rarity, boolean rotated, ServerLevel level, int x, int y, int z) {
        int radius = 3, points = 30;
        double angleBase = 2 * Math.PI / points;
        y +=radius;

        DustColorTransitionOptions options = rarity.getContour();
        double step = 0.5;
        double angle;
        double sX = x + 0.5;
        double sZ = z + 0.5;
        if (rotated) {
            for (int i = 0; i < points; i++) {
                angle = angleBase * i;
                level.sendParticles(options, true, true, sX,
                        y + Math.cos(angle) * radius,
                        sZ + Math.sin(angle) * radius,
                        5, 0.25, 0.25, 0.25,
                        0
                );
            }

            options = rarity.getDustOption();
            for (double r = radius - step; r >= 0; r -= step, points -= 2) {
                angleBase = 2 * Math.PI / points;
                for (int i = 0; i < points; i++) {
                    angle = angleBase * i;
                    level.sendParticles(options, true, true, sX,
                            y + Math.cos(angle) * r,
                            sZ + Math.sin(angle) * r,
                            1, 0.1, 0.1, 0.1,
                            0
                    );
                }
            }
        } else {
            for (int i = 0; i < points; i++) {
                angle = angleBase * i;
                level.sendParticles(options, true, true,
                        sX + Math.cos(angle) * radius,
                        y + Math.sin(angle) * radius,
                        sZ,
                        5, 0.25, 0.25, 0.25,
                        0
                );
            }

            options = rarity.getDustOption();
            for (double r = radius - step; r >= 0; r -= step, points -= 2) {
                angleBase = 2 * Math.PI / points;
                for (int i = 0; i < points; i++) {
                    angle = angleBase * i;
                    level.sendParticles(options, true, true,
                            sX + Math.cos(angle) * r,
                            y + Math.sin(angle) * r,
                            sZ,
                            1, 0.1, 0.1, 0.1,
                            0
                    );
                }
            }
        }
    }
}
