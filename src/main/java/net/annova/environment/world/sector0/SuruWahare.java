package net.annova.environment.world.sector0;

import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.WorldSector;
import net.annova.environment.world.sector0.location.*;
import org.bukkit.World;

import java.util.List;

import static net.annova.environment.world.GlobalWorld.loadWorldAndGet;

public class SuruWahare implements WorldSector {
    public static final World world0 = loadWorldAndGet("world"); // позже надо sector 0

    @Override
    public String name() {
        return "world-suruwahare";
    }

    @Override
    public List<SectorLocation> zones() {
        return List.of(
                new DarkForest(),
                new DryadPedestal(),
                new Meteorite(),
                new Mines(),
                new Oasis(),
                new SpiderSwamps()
        );
    }
}
