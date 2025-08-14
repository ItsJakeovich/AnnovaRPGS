package net.annova.environment.world;

import net.annova.environment.civilization.build.TownBuild;
import net.minecraft.core.Vec3i;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectorGrid {
    private static final int SQUARE_SIZE = 200;

    private final Map<String, List<TownBuild>> squares = new HashMap<>();

    public void addBuild(TownBuild build) {
        Vec3i min = build.getConstruction().getBuildPoint(), max = build.getConstruction().getMaxPoint();

        int minSectorX = min.getX() / SQUARE_SIZE;
        int maxSectorX = max.getX() / SQUARE_SIZE;
        int minSectorZ = min.getZ() / SQUARE_SIZE;
        int maxSectorZ = max.getZ() / SQUARE_SIZE;

        for (int sx = minSectorX; sx <= maxSectorX; sx++) {
            for (int sz = minSectorZ; sz <= maxSectorZ; sz++) {
                squares.computeIfAbsent(sx+":"+sz, k -> new ArrayList<>()).add(build);
            }
        }
    }
    private String getSectorKey(Vec3i pos) {
        return pos.getX()/ SQUARE_SIZE + ":" + pos.getZ()/ SQUARE_SIZE;
    }

    @Nullable
    public TownBuild getBuildAt(Vec3i pos) {
        List<TownBuild> list = squares.get(this.getSectorKey(pos));
        if (list == null) return null;

        for (TownBuild build : list) {
            if (build.getConstruction().intersect(pos)) return build;
        }
        return null;
    }
}
