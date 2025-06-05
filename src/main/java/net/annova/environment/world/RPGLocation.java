package net.annova.environment.world;

import net.annova.environment.world.sector0.location.*;
import net.annova.object.person.RPGPerson;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import static net.annova.environment.world.sector0.SuruWahare.world0;
import static net.annova.main.GlobalRPG.getRPGPerson;

public enum RPGLocation {
    MINES(world0, new Mines()),
    DARK_FOREST(world0, new DarkForest()),
    DRYAD_PEDESTAL(world0, new DryadPedestal()),
    METEORITE(world0, new Meteorite()),
    OASIS(world0, new Oasis()),
    SPIDER_SWAMPS(world0, new SpiderSwamps())
    ;

    private final World world;
    private final Location location;
    RPGLocation(World world, Location location) {
        this.world = world;
        this.location = location;
    }

//    public static final Map<World, Set<RPGLocation>> LOCATIONS_BY_WORLD = new EnumMap<>(RPGLocation.class);
//    static {
//
//    }

    public Location getLocation() {
        return location;
    }

    public static void updatePlayerLocations() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location location = null;
            for (RPGLocation rpg : RPGLocation.values()) {
                Location loc = rpg.getLocation();
                if (loc.isOutside(player.getLocation())) continue;
            }
            RPGPerson person = getRPGPerson(player);
        }
    }
}
