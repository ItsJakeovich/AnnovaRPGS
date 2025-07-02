package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.object.LocationCategory;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.person.RPGPerson;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.entity.core.RPGEntityType.MELANOX;
import static net.annova.object.entity.core.RPGEntityType.getEntitiesByCategory;
import static net.annova.util.BukkitObjects.random;

public class SpiderSwamps extends SectorLocation {
    private final String[] messageKeys = {
            "s0-swamps-msg-0",
            "s0-swamps-msg-1",
            "s0-swamps-msg-2",
            "s0-swamps-msg-3",
            "s0-swamps-msg-4",
            "s0-swamps-msg-5",
            "s0-swamps-msg-6",
            "s0-swamps-msg-7",
    };
    private final Map<Player, Long> messageCooldown = new HashMap<>();

    @Override
    public void activity() {

    }

    @Override
    public void movement(RPGPerson person, Player player) {
        long millis = System.currentTimeMillis();
        if (messageCooldown.containsKey(player) &&  millis < messageCooldown.get(player)) return;
        messageCooldown.put(player, millis + 1800_000); // 30 min

        person.getPerson().send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);
        if (player.getEquipment().getHelmet() == null) person.damage(2.0f);
    }

    private static final EnumSet<RPGEntityType> toSpawn = getEntitiesByCategory(LocationCategory.S0_SWAMPS, MELANOX);
    @Override
    public EnumSet<RPGEntityType> entitiesToSpawn() {
        return toSpawn;
    }

    private static final Location[] points = new org.bukkit.Location[] {
            new org.bukkit.Location(world0, -759, 63, 163)
    };
    @Override
    public org.bukkit.Location[] points() {
        return points;
    }

    @Override
    public int squaredRadius() {
        return 256;
    }
}
