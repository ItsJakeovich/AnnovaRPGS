package net.annova.thread;

import net.annova.environment.dungeon.Dungeon;
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

public class FiveSecondsTimer extends AnnovaTask { // PersonUpdateTimer + DungeonUpdateTimer;
    @Override
    public void run() {
        ServerLevel dungeonLevel = getDungeonWorld();
        for (Dungeon dungeon : getDungeons()) {
            Vec3 vec3Spawn = dungeon.getSpawnPosition();
            Vec3i summon = dungeon.getSummonPosition();
            this.spawnPortalParticles(dungeonLevel, Mth.floor(vec3Spawn.x), Mth.floor(vec3Spawn.y), Mth.floor(vec3Spawn.z));
            this.spawnPortalParticles(dungeon.getSummonLevel(), summon.getX(), summon.getY(), summon.getZ());
        }

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

    private static final DustColorTransitionOptions options = new DustColorTransitionOptions(14540253, 16777215, 1f);
    private void spawnPortalParticles(ServerLevel level, int x, int y, int z) {
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
