package net.annova.environment.civilization.territory;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;
import java.util.Set;

import static net.minecraft.world.level.biome.Biomes.FROZEN_RIVER;
import static net.minecraft.world.level.biome.Biomes.RIVER;

public class AreaChunk {
    private CultureBiome biome;
    private final ChunkPos position;
    private boolean river = false;

    private static final Set<ResourceKey<Biome>> RIVER_BIOMES = Set.of(RIVER, FROZEN_RIVER);
    public AreaChunk(ServerLevel level, ChunkPos position) {
        this.position = position;
        int posX = position.getMinBlockX(), posZ = position.getMinBlockZ();
        loop: for (int z = 0; z < 16; z+=3) {
            for (int x = 0; x < 16; x+=3) {
                Optional<ResourceKey<Biome>> optional = level.getBiome(
                        new BlockPos(posX+x, 62, posZ+z)
                ).unwrapKey();
                if (optional.isPresent() && RIVER_BIOMES.contains(optional.get())) {
                    this.river = true;
                    break loop;
                }
            }
        }
        level.getBiome(new BlockPos(posX+7, 62, posZ+7)
        ).unwrapKey().ifPresent(biomeResourceKey ->
                this.biome = CultureBiome.valueOf(biomeResourceKey.location().getPath().toUpperCase())
        );
    }

    public CultureBiome getBiome() {
        return biome;
    }

    public ChunkPos getPosition() {
        return position;
    }

    public boolean isWater() {
        CultureBiome.Type type = this.biome.getType();
        return type == CultureBiome.Type.RIVER
                || type == CultureBiome.Type.OCEAN;
    }
    public boolean hasRiver() {
        return this.river;
    }
}
