package net.annova.environment.world;

import net.annova.environment.civilization.build.TownBuild;
import net.minecraft.core.Vec3i;

public abstract class WorldSector {
    public abstract String name();
    public abstract SectorLocation[] zones();

    private final SectorGrid sectorGrid = new SectorGrid();

    public void addBuild(TownBuild build) {
        this.sectorGrid.addBuild(build);
    }
    public TownBuild getBuildAt(Vec3i pos) {
        return this.sectorGrid.getBuildAt(pos);
    }
}
