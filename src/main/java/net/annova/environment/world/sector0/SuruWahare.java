package net.annova.environment.world.sector0;

import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.WorldSector;
import net.annova.environment.world.sector0.location.*;
import org.bukkit.World;

import static net.annova.environment.world.GlobalWorld.loadWorldAndGet;

public class SuruWahare implements WorldSector {
    public static final World world0 = loadWorldAndGet("world"); // позже надо sector 0
    /**
     * 0 - Dark Forest
     * 1 - Dryad Pedestal
     * 2 - Meteorite
     * 3 - Mines
     * 4 - Oasis
     * 5 - Spider Swamps
     */
    public static final SectorLocation[] zones = new SectorLocation[]{
            new DarkForest(),
            new DryadPedestal(),
            new Meteorite(),
            new Mines(),
            new Oasis(),
            new SpiderSwamps()
    };

    @Override
    public String name() {
        return "world-suruwahare";
    }

    @Override
    public SectorLocation[] zones() {
        return zones;
    }
}
