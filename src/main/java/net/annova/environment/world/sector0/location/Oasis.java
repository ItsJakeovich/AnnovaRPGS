package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.event.UpdateTask;
import net.annova.environment.world.event.WorldUpdateTimer;
import net.annova.object.LocationCategory;
import net.annova.object.person.Vec3iSelection;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.entity.mobs.custom.CustomOasisBubble;
import net.annova.object.person.RPGPerson;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.object.entity.core.RPGEntityType.*;
import static net.annova.object.person.advancement.RPGAdvancement.*;
import static net.annova.util.BukkitObjects.*;

public class Oasis extends SectorLocation {
    private final String[] messageKeys = {
            "s0-oasis-msg-0",
            "s0-oasis-msg-1",
            "s0-oasis-msg-2",
            "s0-oasis-msg-3",
            "s0-oasis-msg-4",
            "s0-oasis-msg-5",
            "s0-oasis-msg-6",
            "s0-oasis-msg-7",
            "s0-oasis-msg-8",
            "s0-oasis-msg-9",
    };
    private final Map<UUID, Long> messageCooldown = new HashMap<>();

    @Override
    public void movement(RPGPerson person, ServerPlayer player) {
        long millis = System.currentTimeMillis();
        UUID uuid = player.getUUID();
        if (messageCooldown.containsKey(uuid) &&  millis < messageCooldown.get(uuid)) return;
        messageCooldown.put(uuid, millis + 1800_000); // 30 min

        person.getPerson().send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);

        person.completeAdvancement(OASIS); // нашел оазис
    }

    private static final EnumSet<RPGEntityType> toSpawn = getEntitiesByCategory(LocationCategory.S0_OASIS, ANGLER_FISH);
    @Override
    public EnumSet<RPGEntityType> entitiesToSpawn() {
        return toSpawn;
    }

    public Oasis() {
        WorldUpdateTimer.addTask(new UpdateTask() {
            @Override
            public void update() {
                spawnOasisBubble();
            }

            @Override
            public int period() {
                return 240;
            }
        });
    }

    private static final Vec3[] bubblePoints = {
            new Vec3(-1006.5, 62, 483.5),
            new Vec3(-1106.5, 62, 485.5),
            new Vec3(-971.5, 62, 488.5),
    };
    public static ArmorStand bubble = null;
    private static final DustColorTransitionOptions options = new DustColorTransitionOptions(65535, 255, 4.0f);
    public static Vec3 spawnOasisBubble() {
        Vec3 position = bubblePoints[random.nextInt(bubblePoints.length)];
        bubble = new CustomOasisBubble(EntityType.ARMOR_STAND, world0);
        bubble.setPos(position);
        world0.tryAddFreshEntityWithPassengers(bubble);
        bubble.setNoGravity(true);
        bubble.setInvulnerable(true);
        bubble.setInvisible(true);
        bubble.getAttribute(Attributes.SCALE).setBaseValue(3);

        Map<UUID, Integer> timesInBubble = new HashMap<>();
        Map<UUID, Long> cooldowns = new HashMap<>();
        new BukkitRunnable() {
            private int i = -1;

            @Override
            public void run() { i++;
                if (i == 80) {
                    bubble.remove(net.minecraft.world.entity.Entity.RemovalReason.DISCARDED);
                    bubble = null;
                    this.cancel();
                    return;
                }
                if (i < 30) {
                    world0.sendParticles(options, true, true, bubble.getX(), bubble.getY(), bubble.getZ(), 12, 0.4, 0.4, 0.4, 0.0);
                    bubble.teleportRelative(0, 0.8, 0);
                    return;
                }
                for (Player player : world0.getNearbyPlayers(TargetingConditions.DEFAULT, null, bubble.getBoundingBox().inflate(1, 1, 1))) {
                    UUID uuid = player.getUUID();
                    int times = timesInBubble.getOrDefault(uuid, 0) + 1;
                    long cooldown = cooldowns.getOrDefault(uuid, 0L);
                    RPGPerson person = getRPGPerson(player);
                    person.completeAdvancement(DROP_FROM_THE_SKY);
                    long millis = System.currentTimeMillis();
                    if (cooldown == 0 || millis > cooldown) {
                        timesInBubble.put(uuid, times);
                        cooldowns.put(uuid, millis + 60_000);
                        if (times == 3) person.completeAdvancement(BUBBLE_SEEKER);
                    }
                }

                world0.sendParticles(options, true, true, bubble.getX(), bubble.getY(), bubble.getZ(), 36, 1.6, 1.6,  1.6, 0.0);
            }
        }.runTaskTimer(getInstance(), 0, 10L);
        return position;
    }

    private static final Vec3iSelection[] borders = new Vec3iSelection[]{
            new Vec3iSelection(-882, 30, 394, -1151, 110, 511),
    };
    @Override
    public Vec3iSelection[] borders() {
        return borders;
    }

    @Override
    public boolean isProtected() {
        return false;
    }
}
