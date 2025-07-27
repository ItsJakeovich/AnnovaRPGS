package net.annova.environment.civilization.territory;

import net.annova.environment.civilization.Town;
import net.annova.object.SimpleConstruction;
import net.annova.object.SimpleStructure;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.*;

public class AreaChunk {
    private final Town town;
    private final ChunkAccess firstChunk;
    private final List<CultureChunk> chunks;
    private CultureBiome biome;
    private boolean hasRiver;
    private List<SimpleConstruction> constructions = new ArrayList<>();

    public AreaChunk(Town town, ChunkAccess firstChunk, SimpleConstruction... construction) {
        this.town = town;
        this.firstChunk = firstChunk;
        this.constructions.addAll(Arrays.asList(construction));
        this.chunks = this.initChunks();
    }

    public List<CultureChunk> initChunks() {
        Location location = new Location(firstChunk.getWorld(), firstChunk.getX()*16, 58, firstChunk.getZ()*16);
        List<CultureChunk> cultureChunks = new ArrayList<>();
        Map<CultureBiome, Integer> biomes = new HashMap<>();
        for (int z = 0; z < 15; z++) {
            for (int x = 0; x < 15; x++) {
                Location loc = location.clone().add(x*16, 0, z*16);
                switch (loc.getChunk().getBlock(0,58,0).getBiome()) {
                    case RIVER, FROZEN_RIVER -> hasRiver = true;
                }
                switch (loc.getChunk().getBlock(15,58,0).getBiome()) {
                    case RIVER, FROZEN_RIVER -> hasRiver = true;
                }
                switch (loc.getChunk().getBlock(0,58,15).getBiome()) {
                    case RIVER, FROZEN_RIVER -> hasRiver = true;
                }
                switch (loc.getChunk().getBlock(15,58,15).getBiome()) {
                    case RIVER, FROZEN_RIVER -> hasRiver = true;
                }
                CultureBiome chunkBiome = CultureBiome.valueOf(loc.getChunk().getBlock(7,58,7).getBiome().toString());
                CultureChunk cultureChunk = new CultureChunk(this, null, loc.getChunk(), chunkBiome);
                cultureChunks.add(cultureChunk);
                if (!biomes.containsKey(chunkBiome)) biomes.put(chunkBiome, 0);
                biomes.put(chunkBiome, biomes.get(chunkBiome)+1);
                town.addChunk(cultureChunk);
            }
        }
        CultureBiome finalBiome = null;
        int lastInt = 0;
        for (CultureBiome b : biomes.keySet()) {
            if (b.getType().equals(CultureBiome.chunkType.OCEAN)
                    || b.getType().equals(CultureBiome.chunkType.RIVER)
                    || b.getType().equals(CultureBiome.chunkType.COAST)
            ) continue;
            int bInt = biomes.get(b);
            if (finalBiome == null) {
                finalBiome = b;
                lastInt = bInt;
                continue;
            }
            if (bInt > lastInt) {
                finalBiome = b;
                lastInt = bInt;
            }
        }
        this.biome = finalBiome;
        if (!constructions.isEmpty()) {
            for (SimpleConstruction construction : constructions) {
                SimpleStructure structure = construction.getStructure();
                for (int z = 0; z < structure.getMaxZ() / 16; z++) {
                    for (int x = 0; x < structure.getMaxX() / 16; x++) {
                        Chunk chunk = construction.getBuildPoint().clone().add(x * 16, 0, z * 16).getChunk();
                        getTownChunk(chunk).setConstruction(construction);
                    }
                }
            }
        }
        town.addArea(this);
        return cultureChunks;
    }

    public Town getTown() {
        return town;
    }

    public Chunk getFirstChunk() {
        return firstChunk;
    }

    public List<CultureChunk> getChunks() {
        return chunks;
    }

    public CultureBiome getBiome() {
        return biome;
    }

    public boolean isHasRiver() {
        return hasRiver;
    }

    public List<SimpleConstruction> getConstructions() {
        return constructions;
    }

    public void setConstructions(List<SimpleConstruction> constructions) {
        this.constructions = constructions;
    }
}
