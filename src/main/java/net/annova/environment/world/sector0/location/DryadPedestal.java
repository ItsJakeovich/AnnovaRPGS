package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.RPGAdvancement;
import net.annova.object.LocationCategory;
import net.annova.object.Person;
import net.annova.object.entity.core.RPGEntityType;
import org.bukkit.entity.Player;

import java.util.*;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.entity.core.RPGEntityType.getEntitiesByCategory;
import static net.annova.util.BukkitObjects.random;

public class DryadPedestal extends SectorLocation {
    private final String[] messageKeys = {
            "s0-dryad-msg-0",
            "s0-dryad-msg-1",
            "s0-dryad-msg-2",
            "s0-dryad-msg-3",
            "s0-dryad-msg-4",
            "s0-dryad-msg-5",
            "s0-dryad-msg-6",
    };
    private final Map<UUID, Long> messageCooldown = new HashMap<>();

    @Override
    public void activity() {

    }

    @Override
    public void movement(Player player) {
        long millis = System.currentTimeMillis();
        UUID id = player.getUniqueId();
        if (messageCooldown.containsKey(id) && millis < messageCooldown.get(id)) return;
        messageCooldown.put(id, millis + 1800_000); // 30 min


        Person person = getPerson(player);
        person.send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);
    }

    @Override
    public Set<RPGAdvancement> advancements() {
        return Set.of();
    }

    @Override
    public EnumSet<RPGEntityType> entitiesToSpawn() {
        return getEntitiesByCategory(LocationCategory.S0_DRYAD);
    }

    @Override
    public org.bukkit.Location[] points() {
        return new org.bukkit.Location[] {
                new org.bukkit.Location(world0, -466, 71, 242)
        };
    }

    @Override
    public int squaredRadius() {
        return 15625;
    }
}
