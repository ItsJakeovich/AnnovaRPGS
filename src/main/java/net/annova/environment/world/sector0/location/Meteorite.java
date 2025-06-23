package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.RPGAdvancement;
import net.annova.object.LocationCategory;
import net.annova.object.Person;
import net.annova.object.entity.core.RPGEntityType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.entity.core.RPGEntityType.getEntitiesByCategory;
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
    };
    private final Map<Player, Long> messageCooldown = new HashMap<>();

    @Override
    public void activity() {
        Location location = this.points()[0];
        location.getWorld().spawnParticle(Particle.FLAME, location, 400, 40, 40, 40);
    }

    @Override
    public void movement(Player player) {
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

        long millis = System.currentTimeMillis();
        if (messageCooldown.containsKey(player) && millis < messageCooldown.get(player)) return;
        messageCooldown.put(player, millis + 1800_000); // 30 min

        Person person = getPerson(player);
        person.send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);
    }

    @Override
    public Set<RPGAdvancement> advancements() {
        return Set.of();
    }

    @Override
    public EnumSet<RPGEntityType> entitiesToSpawn() {
        return getEntitiesByCategory(LocationCategory.S0_METEORITE);
    }

    @Override
    public org.bukkit.Location[] points() {
        return new org.bukkit.Location[] {
                new org.bukkit.Location(world0, 715, 70, -183)
        };
    }

    @Override
    public int squaredRadius() {
        return 3600;
    }
}