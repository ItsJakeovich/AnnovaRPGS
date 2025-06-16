package net.annova.environment.world;

import java.util.List;

public interface WorldSector {
    String name();
    List<SectorLocation> zones();
}
