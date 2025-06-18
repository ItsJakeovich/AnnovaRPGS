package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.RPGAdvancement;
import net.annova.object.LocationCategory;
import net.annova.object.Person;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.RPGPerson;
import net.annova.thread.event.HourTickEvent;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.entity.core.RPGEntityType.getEntitiesByCategory;
import static net.annova.object.item.base.ItemType.UNKNOWN_SOURCE_WATER;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.util.BukkitObjects.random;
import static net.annova.environment.world.RPGAdvancement.*;

public class Oasis extends SectorLocation implements Listener {
    //public static final org.bukkit.Location theOasis = new org.bukkit.Location(world0, -1023, 72, 412);;
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
    public void movement(Player player) {
        if (this.isOutside(player.getLocation())) return;

        long millis = System.currentTimeMillis();
        if (messageCooldown.containsKey(player) &&  millis < messageCooldown.get(player)) return;
        messageCooldown.put(player, millis + 1800_000); // 30 min

        Person person = getPerson(player);
        person.send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);

        getRPGPerson(person).completeAdvancement(player, OASIS); // нашел оазис
    }

    @Override
    public Set<RPGAdvancement> advancements() {
        return Set.of(
                OASIS,
                BUBBLE_SEEKER,
                CRAB_IS_HAPPY,
                DROP_FROM_THE_SKY,
                FRIENDS_ON_SCALES,
                GIME_ME_CRAB,
                GOOD_BITE,
                MOON_FISHERMAN,
                PACIFICATION,
                SWIM_LITTLE_FISH,
                THATS_LUCK,
                GOOD_CATCH
        );
    }

    @Override
    public Set<RPGEntityType> entitiesToSpawn() {
        return getEntitiesByCategory(LocationCategory.S0_OASIS);
    }

    @Override
    public org.bukkit.Location[] points() {
        return new org.bukkit.Location[]{
                new org.bukkit.Location(world0, -1092, 76, 451),
                new org.bukkit.Location(world0, -971, 76, 451),
                new org.bukkit.Location(world0, -933, 76, 451),
        };
    }

    @Override
    public int squaredRadius() {
        return 3660;
    }

    private final org.bukkit.Location[] bubblePoints = {
            new org.bukkit.Location(world0, -1006, 69, 483),
            new org.bukkit.Location(world0, -1106, 69, 485),
            new org.bukkit.Location(world0, -971, 69, 488),
    };
    int bubbleCooldown = 0;
    private ArmorStand bubble = null;
    @EventHandler
    public void onHourTick(HourTickEvent event) {
        if (bubbleCooldown != 0) {
            bubbleCooldown++;
            return;
        }
        bubbleCooldown = -3;

        org.bukkit.Location point = bubblePoints[random.nextInt(bubblePoints.length)].clone();
        Oasis.this.bubble = (ArmorStand) world0.spawnEntity(point, EntityType.ARMOR_STAND);
        bubble.setGravity(false);
        bubble.setInvulnerable(true);
        bubble.setVisible(false);
        bubble.setSmall(true);

        Map<Player, Integer> timesInBubble = new HashMap<>();
        Map<Player, Long> cooldowns = new HashMap<>();
        new BukkitRunnable() {
            private int i = 0;

            @Override
            public void run() {
                if (i == 80) {
                    bubble.remove();
                    bubble = null;
                    this.cancel();
                    return;
                }
                if (i < 20) {
                    world0.spawnParticle(Particle.DUST_COLOR_TRANSITION, point, 4, 0.2, 0.2, 0.2, 0, new Particle.DustTransition(Color.BLUE, Color.AQUA, 4.0F));
                    point.add(0, 1, 0);
                    bubble.teleport(point);
                    i++;
                    return;
                }
                for (Entity entity : bubble.getNearbyEntities(1, 1, 1)) {
                    if (entity instanceof Player player) {
                        int times = timesInBubble.getOrDefault(player, 0) + 1;
                        long cooldown = cooldowns.getOrDefault(player, 0L);
                        RPGPerson person = getRPGPerson(player);
                        person.completeAdvancement(player, DROP_FROM_THE_SKY);
                        long millis = System.currentTimeMillis();
                        if (cooldown == 0 || millis > cooldown) {
                            timesInBubble.put(player, times);
                            cooldowns.put(player, millis + 60_000);
                            if (times == 3) person.completeAdvancement(player, BUBBLE_SEEKER);
                        }
                    }
                }

                world0.spawnParticle(Particle.DUST_COLOR_TRANSITION, point, 16, 0.6, 0.6, 0.6, 0, new Particle.DustTransition(Color.BLUE, Color.AQUA, 4.0F));
            }
        }.runTaskTimer(getInstance(), 0, 10L);
    }
    @EventHandler
    public void bottleClickBubble(PlayerInteractAtEntityEvent event) {
        if (!event.getRightClicked().equals(bubble)) return;

        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        if (!inventory.getItemInMainHand().getType().equals(Material.GLASS_BOTTLE)) return;

        inventory.setItemInMainHand(UNKNOWN_SOURCE_WATER.create(player));
        player.playSound(player, Sound.ITEM_BOTTLE_FILL, 1, 1);
    }
}
