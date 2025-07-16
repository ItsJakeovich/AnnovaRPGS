package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.event.UpdateTask;
import net.annova.environment.world.event.WorldUpdateTimer;
import net.annova.object.LocationCategory;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.item.base.ItemType;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.ItemEquipment;
import net.annova.object.person.RPGPerson;
import net.annova.thread.event.HourTickEvent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_21_R4.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.annova.environment.world.GlobalWorld.isNight;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.entity.core.RPGEntityType.ANGLER_FISH;
import static net.annova.object.entity.core.RPGEntityType.getEntitiesByCategory;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.object.item.base.ItemType.*;
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
    private final Map<Player, Long> messageCooldown = new HashMap<>();

    @Override
    public void movement(RPGPerson person, Player player) {
        long millis = System.currentTimeMillis();
        if (messageCooldown.containsKey(player) &&  millis < messageCooldown.get(player)) return;
        messageCooldown.put(player, millis + 1800_000); // 30 min

        person.getPerson().send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);

        person.completeAdvancement(OASIS); // нашел оазис
    }

    private static final EnumSet<RPGEntityType> toSpawn = getEntitiesByCategory(LocationCategory.S0_OASIS, ANGLER_FISH);
    @Override
    public EnumSet<RPGEntityType> entitiesToSpawn() {
        return toSpawn;
    }

    private static final Location[] points = new org.bukkit.Location[]{
            new org.bukkit.Location(world0, -1092, 76, 451),
            new org.bukkit.Location(world0, -971, 76, 451),
            new org.bukkit.Location(world0, -933, 76, 451),
    };
    @Override
    public org.bukkit.Location[] points() {
        return points;
    }

    @Override
    public int squaredRadius() {
        return 3660;
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

    private static final org.bukkit.Location[] bubblePoints = {
            new org.bukkit.Location(world0, -1006, 69, 483),
            new org.bukkit.Location(world0, -1106, 69, 485),
            new org.bukkit.Location(world0, -971, 69, 488),
    };
    public static ArmorStand bubble = null;
    public static Location spawnOasisBubble() {
        Location point = bubblePoints[random.nextInt(bubblePoints.length)].clone();
        bubble = (ArmorStand) world0.spawnEntity(point.clone().add(0, -3, 0), EntityType.ARMOR_STAND);
        bubble.setGravity(false);
        bubble.setInvulnerable(true);
        bubble.setVisible(false);
        bubble.getAttribute(Attribute.SCALE).setBaseValue(3);

        Map<Player, Integer> timesInBubble = new HashMap<>();
        Map<Player, Long> cooldowns = new HashMap<>();
        new BukkitRunnable() {
            private int i = -1;

            @Override
            public void run() { i++;
                if (i == 80) {
                    bubble.remove();
                    bubble = null;
                    this.cancel();
                    return;
                }
                if (i < 30) {
                    world0.spawnParticle(Particle.DUST_COLOR_TRANSITION, point, 12, 0.4, 0.4, 0.4, 0, new Particle.DustTransition(Color.AQUA, Color.BLUE, 4.0F), true);
                    point.add(0, 0.8, 0);
                    bubble.teleport(point);
                    return;
                }
                for (Entity entity : bubble.getNearbyEntities(1, 1, 1)) {
                    if (entity instanceof Player player) {
                        int times = timesInBubble.getOrDefault(player, 0) + 1;
                        long cooldown = cooldowns.getOrDefault(player, 0L);
                        RPGPerson person = getRPGPerson(player);
                        person.completeAdvancement(DROP_FROM_THE_SKY);
                        long millis = System.currentTimeMillis();
                        if (cooldown == 0 || millis > cooldown) {
                            timesInBubble.put(player, times);
                            cooldowns.put(player, millis + 60_000);
                            if (times == 3) person.completeAdvancement(BUBBLE_SEEKER);
                        }
                    }
                }

                world0.spawnParticle(Particle.DUST_COLOR_TRANSITION, point, 36, 1.6, 1.6, 1.6, 0, new Particle.DustTransition(Color.AQUA, Color.BLUE, 4.0F), true);
            }
        }.runTaskTimer(getInstance(), 0, 10L);
        return point;
    }
}
