package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.object.LocationCategory;
import net.annova.object.Person;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.RPGPerson;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.entity.core.RPGEntityType.*;
import static net.annova.object.item.base.ArmorSet.CHITIN;
import static net.annova.util.BukkitObjects.random;

public class Meteorite extends SectorLocation {
    private final String[] messageKeys = {
            "s0-meteorite-msg-0",
            "s0-meteorite-msg-1",
            "s0-meteorite-msg-2",
            "s0-meteorite-msg-3",
            "s0-meteorite-msg-4",
            "s0-meteorite-msg-5",
            "s0-meteorite-msg-6",
            "s0-meteorite-msg-7",
            "s0-meteorite-msg-8",
    };
    private final Map<Player, Long> messageCooldown = new HashMap<>();

    private int i = 20; // 20 минут
    @Override
    public void activity() {
        if (i != 0) {
            i--;
            return;
        } i = 20;
        Location location = this.points()[0];
        World world = location.getWorld();
        world.spawnParticle(Particle.FLAME, location, 400, 28, 20, 28);
        for (Entity entity : world.getNearbyEntities(location, 32, 24, 32)) {
            if (entity instanceof Player) {
                RPGPerson person = getRPGPerson(entity);
                SHARDS.tryNaturalSpawn(entity.getLocation());

                if (CHITIN == person.getArmorSet()) continue;
            }
            entity.setFireTicks(250);
            if (entity instanceof LivingEntity living) living.damage(4.0);
        }
    }

    @Override
    public void movement(RPGPerson person, Player player) {
        boolean isDamageable = true;
        if (CHITIN == person.getArmorSet()) isDamageable = false;
        if (isDamageable) {
            double pDistanceSquared = player.getLocation().distanceSquared(this.points()[0]);
            player.setFireTicks(120);
            if (player.getGameMode() == GameMode.SURVIVAL) {
                double damage;
                int fireTicks;
                if (pDistanceSquared <= 576) {
                    damage = 16;
                    fireTicks = 600;
                } else {
                    double koefficient = 1 - pDistanceSquared / 3600;
                    damage = 6 * koefficient;
                    fireTicks = (int) (200 * koefficient);
                }
                player.setFireTicks(fireTicks);
                player.damage(damage);
            }
        }

        long millis = System.currentTimeMillis();
        if (messageCooldown.containsKey(player) && millis < messageCooldown.get(player)) return;
        messageCooldown.put(player, millis + 1800_000); // 30 min

        person.getPerson().send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);
    }

    private static final EnumSet<RPGEntityType> toSpawn = getEntitiesByCategory(LocationCategory.S0_METEORITE, METEORITE_GOLEM);
    @Override
    public EnumSet<RPGEntityType> entitiesToSpawn() {
        return toSpawn;
    }

    private static final Location[] points = new org.bukkit.Location[] {
            new org.bukkit.Location(world0, 715, 70, -183)
    };
    @Override
    public org.bukkit.Location[] points() {
        return points;
    }

    @Override
    public int squaredRadius() {
        return 3600;
    }
}