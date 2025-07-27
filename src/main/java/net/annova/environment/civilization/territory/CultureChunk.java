package net.annova.environment.civilization.territory;

import net.annova.object.SimpleConstruction;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.bukkit.Chunk;

public class CultureChunk {
    public AreaChunk area;
    public SimpleConstruction construction;
    public ChunkAccess chunk;
    public CultureBiome biome;

    public CultureChunk(AreaChunk area, SimpleConstruction construction, Chunk chunk, CultureBiome biome) {
        this.area = area;
        this.construction = construction;
        this.chunk = chunk;
        this.biome = biome;
    }

    public AreaChunk getArea() {
        return area;
    }

    public SimpleConstruction getConstruction() {
        return construction;
    }

    public void setConstruction(SimpleConstruction construction) {
        this.construction = construction;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public CultureBiome getBiome() {
        return biome;
    }
}
