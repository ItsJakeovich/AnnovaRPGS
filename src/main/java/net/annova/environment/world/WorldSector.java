package net.annova.environment.world;

import net.annova.environment.civilization.build.TownBuild;
import net.annova.object.person.Vec3iSelection;
import net.minecraft.core.Vec3i;

public abstract class WorldSector {
    public abstract String name();
    public abstract SectorLocation[] zones();
    public abstract Vec3iSelection border();
    public abstract boolean isPVE();

    private final SectorGrid sectorGrid = new SectorGrid();

    public void addBuild(TownBuild build) {
        this.sectorGrid.addBuild(build);
    }
    public TownBuild getBuildAt(Vec3i pos) {
        return this.sectorGrid.getBuildAt(pos);
    }
}
