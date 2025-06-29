package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.object.LocationCategory;
import net.annova.object.Person;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.item.base.ItemType;
import net.annova.object.item.base.attribute.ItemEquipment;
import net.annova.object.person.RPGPerson;
import net.annova.thread.event.HourTickEvent;
import org.bukkit.*;
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
import static net.annova.object.entity.core.RPGEntityType.getEntitiesByCategory;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.object.item.base.ItemType.*;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;
import static net.annova.object.person.advancement.RPGAdvancement.*;

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
    public void activity() {

    }

    @Override
    public void movement(Player player) {
        long millis = System.currentTimeMillis();
        if (messageCooldown.containsKey(player) &&  millis < messageCooldown.get(player)) return;
        messageCooldown.put(player, millis + 1800_000); // 30 min

        RPGPerson person = getRPGPerson(player);
        person.getPerson().send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);

        person.completeAdvancement(OASIS); // нашел оазис
    }

    @Override
    public EnumSet<RPGEntityType> entitiesToSpawn() {
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

    private static final org.bukkit.Location[] bubblePoints = {
            new org.bukkit.Location(world0, -1006, 69, 483),
            new org.bukkit.Location(world0, -1106, 69, 485),
            new org.bukkit.Location(world0, -971, 69, 488),
    };
    int bubbleCooldown = 0;
    private static ArmorStand bubble = null;
    @EventHandler
    public void onHourTick(HourTickEvent event) {
        if (bubbleCooldown != 0) {
            bubbleCooldown++;
            return;
        }
        bubbleCooldown = -3;
        spawnOasisBubble();
    }
    public static Location spawnOasisBubble() {
        Location point = bubblePoints[random.nextInt(bubblePoints.length)].clone();
        bubble = (ArmorStand) world0.spawnEntity(point, EntityType.ARMOR_STAND);
        bubble.setGravity(false);
        bubble.setInvulnerable(true);
        bubble.setVisible(false);

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
                    world0.spawnParticle(Particle.DUST_COLOR_TRANSITION, point, 12, 0.4, 0.4, 0.4, 0, new Particle.DustTransition(Color.BLUE, Color.AQUA, 4.0F), true);
                    point.add(0, 0.5, 0);
                    bubble.teleport(point);
                    i++;
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

                world0.spawnParticle(Particle.DUST_COLOR_TRANSITION, point, 16, 1.6, 1.6, 1.6, 0, new Particle.DustTransition(Color.BLUE, Color.AQUA, 4.0F), true);
            }
        }.runTaskTimer(getInstance(), 0, 10L);
        return point;
    }

    @EventHandler
    public void bottleClickBubble(PlayerInteractAtEntityEvent event) {
        if (!event.getRightClicked().equals(bubble)) return;

        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        if (!inventory.getItemInMainHand().getType().equals(Material.GLASS_BOTTLE)) return;

        inventory.setItemInMainHand(UNKNOWN_SOURCE_WATER.create(player));
        player.playSound(player, Sound.ITEM_BOTTLE_FILL, 1, 1);
        getRPGPerson(player).completeAdvancement(WATER);
    }

    public static final Map<ItemType, Double> OASIS_FISHS = new HashMap<>() {{
        this.put(ANGEL_FISH, 0.007);
        this.put(ANGLER_FISH_SPAWN_EGG, 0.005);
        this.put(AZURE_FISH, 0.01);
        this.put(BARK_BEETLE_FISH, 0.04);
        this.put(BLOB_FISH_BLUE, 0.01);
        this.put(BLOB_FISH_GRAY, 0.01);
        this.put(BLOB_FISH_YELLOW, 0.01);
        this.put(CRAB_CLAW, 0.01);
        this.put(ELK_FISH, 0.025);
        this.put(FIRE_FISH, 0.037);
        this.put(FURY_CAVIAR, 0.012);
        this.put(KOI_FISH, 0.02);
        this.put(NALIM_FISH, 0.0184);
        this.put(OASIS_FISH, 0.005);
        this.put(PEARL, 0.008);
        this.put(ROYAL_PUFFERFISH, 0.004);
        this.put(SEASMITH, 0.04);
        this.put(SILVER_PUMP, 0.01);
        this.put(VOLCANIC_STREAMS_MALMA, 0.03);
        this.put(WHALE_FISH, 0.0121);

        this.put(TIDE_BLADE, 0.008);
    }};
    public static final List<ItemType> FISHS = List.copyOf(OASIS_FISHS.keySet());
    @EventHandler
    public void fishing(PlayerFishEvent event) {
        RPGPerson person = getRPGPerson(event.getPlayer());
        if (person.getSectorLocation() instanceof Oasis) {
            if (chance(0.7)) return; // 70% на обычную рыбу

            ItemType finalFish;
            while (true) {
                ItemType fish = FISHS.get(random.nextInt(FISHS.size()));
                if (chance(OASIS_FISHS.get(fish))) {
                    finalFish = fish;
                    break;
                }
            }
            Item item = (Item) event.getCaught();
            item.setItemStack(finalFish.create(person, 1));

            person.completeAdvancement(GOOD_BITE);
            if (finalFish.equals(OASIS_FISH)) person.completeAdvancement(THATS_LUCK);
            if (isNight(world0)) {
                person.completeAdvancement(MOON_FISHERMAN);
            }
            if (finalFish.getItem() instanceof ItemEquipment) {
                person.catchedTreasures++;
                person.completeAdvancement(WOW_BEAUTIFUL);
                if (person.catchedTreasures >= 10) person.completeAdvancement(GOOD_DAY);
                if (person.catchedTreasures >= 100) person.completeAdvancement(GOLD_MANIA);
            }
        }
    }
}
