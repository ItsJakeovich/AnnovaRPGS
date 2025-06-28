package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.object.LocationCategory;
import net.annova.object.Person;
import net.annova.object.entity.core.RPGEntityType;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.entity.core.RPGEntityType.getEntitiesByCategory;
import static net.annova.util.BukkitObjects.random;

public class Mines extends SectorLocation {
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
    public void activity() {

    }

    @Override
    public void movement(Player player) {
        long millis = System.currentTimeMillis();
        if (messageCooldown.containsKey(player) &&  millis < messageCooldown.get(player)) return;
        messageCooldown.put(player, millis + 1800_000); // 30 min

        Person person = getPerson(player);
        person.send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);
    }

    @Override
    public EnumSet<RPGEntityType> entitiesToSpawn() {
        return getEntitiesByCategory(LocationCategory.S0_MINES);
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
