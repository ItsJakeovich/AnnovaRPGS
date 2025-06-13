package net.annova.environment.world.sector0;

import net.annova.environment.world.Location;
import net.annova.environment.world.WorldSector;
import net.annova.environment.world.sector0.location.*;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

import static net.annova.environment.world.GlobalWorld.loadWorldAndGet;

public class SuruWahare implements WorldSector {
    public static final  World world0 = loadWorldAndGet("world"); // позже надо sector 0
    public static final List<Location> sectorZones = List.of(
            new DarkForest(),
            new DryadPedestal(),
            new Meteorite(),
            new Mines(),
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
