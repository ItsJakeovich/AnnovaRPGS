package net.annova.thread;

import net.annova.command.cmdRPG.RPGCode;
import net.annova.environment.dungeon.Dungeon;
import net.annova.environment.world.RPGPortal;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import net.annova.object.person.status.StatusType;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static net.annova.environment.dungeon.GlobalDungeon.*;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.RPGRarity.*;

public class FiveSecondsTimer extends AnnovaTask { // PersonUpdateTimer + DungeonUpdateTimer;
    @Override
    public void run() {
        startPortalTick();
        // Телепорт чуть ниже (transition)

        for (Player player : Bukkit.getOnlinePlayers()) {
            RPGPerson person = getRPGPerson(player);
            ServerPlayer server = person.getServerPlayer();

            TeleportTransition transition = getPortalTransition(server.blockPosition());
            if (transition != null) server.teleport(transition);

            person.updateSectorLocation();

            StatusManager manager = person.getStatusManager();
            for (StatusType type : StatusType.getForActivation()) {
                if (manager.hasStatus(type)) continue;
                if (!type.getActivationItem().has(player)) continue; // если у игрока нет предмета
                manager.addStatus(type);
                server.playSound(SoundEvents.WANDERING_TRADER_YES, 1, 1);
            }
            ItemStack item = player.getInventory().getItemInMainHand();
            if (!item.getType().isAir()) {
                for (StatusType type : StatusType.getForActivationInHand()) {
                    if (manager.hasStatus(type)) continue;
                    if (type.getActivationInHandItem().equals(item)) {
                        manager.addStatus(type);
                    }
                }
            }
            person.getEquipment().update();

            person.updateHealth();
            person.regen();
        }
    }

    public static void startPortalTick() {
        // World portals
        RPGPortal portal = RPGPortal.RPG_ENTER;
        spawnPortalParticles((RPGCode.ALLOW_TO_JOIN ? UNCOMMON : IMPOSSIBLE).getDustOption(),
                portal.getLevel(), portal.getX(), portal.getY(), portal.getZ()
        );
        // Dungeon portals
        ServerLevel dungeonLevel = getDungeonWorld();
        for (Dungeon dungeon : getDungeons()) {
            Vec3 vec3Spawn = dungeon.getSpawnPosition();
            Vec3i summon = dungeon.getSummonPosition();
            spawnPortalParticles(COMMON.getDustOption(), dungeonLevel, Mth.floor(vec3Spawn.x), Mth.floor(vec3Spawn.y), Mth.floor(vec3Spawn.z));
            spawnPortalParticles(COMMON.getDustOption(), dungeon.getSummonLevel(), summon.getX(), summon.getY(), summon.getZ());
        }
    }
    private static void spawnPortalParticles(DustColorTransitionOptions options, ServerLevel level, int x, int y, int z) {
        int radius = 8, points = 16;
        double angleBase = 2 * Math.PI / points;
        for (int i = 0; i < points; i++) {
            double angle = angleBase*i;
            level.sendParticles(options, x,
                    y+Math.cos(angle)*radius,
                    z+Math.sin(angle)*radius,
                    8, 0.5, 0.5, 0.5,
                    0
            );
        }
    }
}
