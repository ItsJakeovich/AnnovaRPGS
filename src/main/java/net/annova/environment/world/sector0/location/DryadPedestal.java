package net.annova.environment.world.sector0.location;

import net.annova.environment.world.SectorLocation;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.advancement.RPGAdvancement;
import net.annova.object.LocationCategory;
import net.annova.object.Person;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.thread.event.HourTickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.entity.core.RPGEntityType.*;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class DryadPedestal extends SectorLocation implements Listener {
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
    public void movement(RPGPerson person, Player player) {
        long millis = System.currentTimeMillis();
        UUID id = player.getUniqueId();
        if (messageCooldown.containsKey(id) && millis < messageCooldown.get(id)) return;
        messageCooldown.put(id, millis + 1800_000); // 30 min

        person.getPerson().send(getInstance(), messageKeys[random.nextInt(messageKeys.length)]);
    }

    private static final EnumSet<RPGEntityType> toSpawn = getEntitiesByCategory(LocationCategory.S0_DRYAD/*, FANG, WEREWOLF*/);
    @Override
    public EnumSet<RPGEntityType> entitiesToSpawn() {
        return toSpawn;
    }

    private static final Location[] points = new org.bukkit.Location[] {
            new org.bukkit.Location(world0, -466, 71, 242)
    };
    @Override
    public org.bukkit.Location[] points() {
        return points;
    }

    @Override
    public int squaredRadius() {
        return 15625;
    }


    @EventHandler
    public void tick(HourTickEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (this == getRPGPerson(player).getSectorLocation() && chance(0.1)) {
                player.playSound(player, "location.postament.songdriada.songdriada", 1, 1);
            }
        }
    }
}
