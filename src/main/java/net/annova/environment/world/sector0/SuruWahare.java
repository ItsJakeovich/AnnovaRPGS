package net.annova.environment.world.sector0;

import net.annova.environment.world.SectorLocation;
import net.annova.environment.world.WorldSector;
import net.annova.environment.world.sector0.location.*;
import net.annova.object.person.Vec3iSelection;
import net.minecraft.server.level.ServerLevel;

import static net.annova.util.BukkitObjects.getWorld;

public class SuruWahare extends WorldSector {
    public static final ServerLevel world0 = getWorld("S0"); // позже надо sector 0, или не надо? НАДО.
    /**
     * 0 - Dark Forest
     * 1 - Dryad Pedestal
     * 2 - Meteorite
     * 3 - Mines
     * 4 - Oasis
     * 5 - Spider Swamps
     */
    public static final SectorLocation[] zones = new SectorLocation[]{
            new ChernuPortal(),
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

    private final Vec3iSelection border = new Vec3iSelection(-1152, 63, -512, 1152, 64, 512);
    @Override
    public Vec3iSelection border() {
        return border;
    }

    @Override
    public boolean isPVE() {
        return true;
    }
}
