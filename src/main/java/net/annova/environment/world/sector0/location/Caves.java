package net.annova.environment.world.sector0.location;

import net.annova.environment.world.Location;
import net.annova.object.Person;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.random;

public class Caves implements Location {
    private final org.bukkit.Location enterPoint = new org.bukkit.Location(world0, -1111, 64, 357);

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
        long millis = System.currentTimeMillis();
        if (messageCooldown.containsKey(player) &&  millis < messageCooldown.get(player)) return;
        messageCooldown.put(player, millis + 1800_000); // 30 min

        if (this.isOutside(64, player.getLocation(), enterPoint)) return;

        Person person = getPerson(player);
        person.send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);
    }
}
