package net.annova.environment.world.sector0.location;

import net.annova.environment.world.Location;
import net.annova.environment.world.advancement.RPGAdvancement;
import net.annova.environment.world.sector0.SuruWahare;
import net.annova.object.Person;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.random;

public class Caves extends SuruWahare implements Location {
    private final String[] messageKeys = {
            "s0-caves-msg-0",
            "s0-caves-msg-1",
            "s0-caves-msg-2",
            "s0-caves-msg-3",
            "s0-caves-msg-4",
            "s0-caves-msg-5",
            "s0-caves-msg-6",
            "s0-caves-msg-7",
            "s0-caves-msg-8",
            "s0-caves-msg-9",
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
    }

    @Override
    public Set<RPGAdvancement> advancements() {
        return Set.of();
    }

    @Override
    public org.bukkit.Location[] points() {
        return new org.bukkit.Location[] {
                new org.bukkit.Location(world0, -1111, 64, 357)
        };
    }

    @Override
    public int squaredRadius() {
        return 64;
    }
}
