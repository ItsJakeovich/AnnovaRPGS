package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.event.UpdateTask;
import net.annova.environment.world.event.WorldUpdateTimer;
import net.annova.object.LocationCategory;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.advancement.RPGAdvancement;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getAmountByTypeEntity;
import static net.annova.object.entity.core.RPGEntityType.*;
import static net.annova.object.item.base.ItemType.MAGIC_SCRAP;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class Mines extends SectorLocation {
    public static final int[][] explosionLocations = new int[][] {
            new int[]{-888, -30, 358},
            new int[]{-935, -30, 357},
            new int[]{-932, -25, 401},
            new int[]{-895, -28, 402},
            new int[]{-941, -26, 304},
            new int[]{-957, -25, 263},
            new int[]{-991, -24, 360}
    };
    public Mines() {
        WorldUpdateTimer.addTask(new UpdateTask() {
            @Override
            public void update() {
                int[] xyz = explosionLocations[random.nextInt(explosionLocations.length)];
                int x = xyz[0], y = xyz[1], z = xyz[2];
                world0.spawnParticle(Particle.DUST_COLOR_TRANSITION, x, y, z, 36, 1.5, 1.2, 1.5, 0, new Particle.DustTransition(Color.AQUA, Color.GRAY, 3.0f), true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () -> {
                    world0.createExplosion(x, y, z, 6, false, false);

                    Location location = new Location(world0, x, y, z);
                    if (getAmountByTypeEntity(PHANTOM) <= 10 && chance(0.05)) PHANTOM.getEntity().create(location);

                    ServerLevel level = ((CraftWorld) world0).getHandle();
                    ItemEntity entity = new ItemEntity(level, x, y, z, MAGIC_SCRAP.create(1));
                    level.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
                }, 80);
            }

            @Override
            public int period() {
                return 60;
            }
        });
    }

    private final String[] messageKeys = {
            "s0-mines-msg-0",
            "s0-mines-msg-1",
            "s0-mines-msg-2",
            "s0-mines-msg-3",
            "s0-mines-msg-4",
            "s0-mines-msg-5",
            "s0-mines-msg-6",
            "s0-mines-msg-7",
            "s0-mines-msg-8",
            "s0-mines-msg-9",
    };
    private final Map<Player, Long> messageCooldown = new HashMap<>();

    @Override
    public void movement(RPGPerson person, Player player) {
        long millis = System.currentTimeMillis();
        if (messageCooldown.containsKey(player) &&  millis < messageCooldown.get(player)) return;
        messageCooldown.put(player, millis + 1800_000); // 30 min

        person.getPerson().send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);

        person.completeAdvancement(RPGAdvancement.MINES);
    }

    private static final EnumSet<RPGEntityType> toSpawn = getEntitiesByCategory(LocationCategory.S0_MINES, GOLEM_BOSS);
    @Override
    public EnumSet<RPGEntityType> entitiesToSpawn() {
        return toSpawn;
    }

    private static final Location[] points = new Location[] {
            new org.bukkit.Location(world0, -1111, 64, 357)
    };
    @Override
    public org.bukkit.Location[] points() {
        return points;
    }

    @Override
    public int squaredRadius() {
        return 64;
    }
}
