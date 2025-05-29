package net.annova.environment.world.sector0.location;

import net.annova.environment.world.Location;
import net.annova.object.Person;
import net.annova.thread.event.HourTickEvent;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

import static net.annova.item.base.Item.UNKNOWN_SOURCE_WATER;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.util.BukkitObjects.random;

public class Oasis implements Location, Listener {
    //public static final org.bukkit.Location theOasis = new org.bukkit.Location(world0, -1023, 72, 412);;
    private final org.bukkit.Location[] locPoints = {
            new org.bukkit.Location(world0, -991, 69, 416),
            new org.bukkit.Location(world0, -1096, 70, 422),
            new org.bukkit.Location(world0, -921, 73, 446)
    };

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
    private final Map<Player, Integer> messageCooldown = new HashMap<>();
    @Override
    public void movement(Player player) {
        if (messageCooldown.containsKey(player)) {
            int min5 = messageCooldown.get(player);
            if (min5 != 0) {
                messageCooldown.put(player, min5+1);
                return;
            }
        }
        messageCooldown.put(player, -5);

        if (this.isOutside(1600, player.getLocation(), locPoints)) return;

        Person person = getPerson(player);
        person.send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);
    }

    private ArmorStand bubble = null;
    private final org.bukkit.Location[] bubblePoints = {
            new org.bukkit.Location(world0, -1006, 69, 483),
            new org.bukkit.Location(world0, -1106, 69, 485),
            new org.bukkit.Location(world0, -971, 69, 488),
    };
    int bubbleCooldown = 0;
    @EventHandler
    public void onHourTick(HourTickEvent event) {
        if (bubbleCooldown != 0) {
            bubbleCooldown++;
            return;
        }
        bubbleCooldown = -3;

        org.bukkit.Location point = bubblePoints[random.nextInt(bubblePoints.length)].clone();
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
                    world0.spawnParticle(Particle.DUST_COLOR_TRANSITION, point, 4, 0.2, 0.2, 0.2, 0, new Particle.DustOptions(Color.AQUA, 4.0F));
                    point.add(0, 1, 0);
                    i++;
                    return;
                } else if (i == 20) {
                    ArmorStand bubble = (ArmorStand) world0.spawnEntity(point, EntityType.ARMOR_STAND);
                    Oasis.this.bubble = bubble;
                    bubble.setGravity(false);
                    bubble.setInvulnerable(true);
                    bubble.setVisible(false);
                    bubble.setSmall(true);
                }
                world0.spawnParticle(Particle.DUST_COLOR_TRANSITION, point, 16, 0.6, 0.6, 0.6, 0, new Particle.DustOptions(Color.AQUA, 4.0F));
            }
        }.runTaskTimer(getInstance(), 0, 10L);
    }
    @EventHandler
    public void bottleClickBubble(PlayerInteractAtEntityEvent event) {
        if (!event.getRightClicked().equals(bubble)) return;

        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        if (!inventory.getItemInMainHand().getType().equals(Material.GLASS_BOTTLE)) return;

        inventory.setItemInMainHand(UNKNOWN_SOURCE_WATER.create());
        player.playSound(player, Sound.ITEM_BOTTLE_FILL, 1, 1);
    }
}
