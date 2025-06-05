package net.annova.environment.world.sector0;

import net.annova.environment.world.Location;
import net.annova.environment.world.WorldSector;
import net.annova.environment.world.sector0.location.*;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;

import static net.annova.environment.world.GlobalWorld.loadWorldAndGet;

public class SuruWahare implements WorldSector {
    public static final  World world0 = loadWorldAndGet("sector0");
    public static final Set<Location> sectorZones = Set.of(
            new Mines(),
            new DryadPedestal(),
            new DarkForest(),
            new Meteorite(),
            new Oasis(),
            new SpiderSwamps()
    );

    @Override
    public String name() {
        return "world-suruwahare";
    }

    @Override
    public void movement() {
        for (Player player : world0.getPlayers()) {
            for (Location location : sectorZones) {
                location.movement(player);
            }
        }
    }
}
