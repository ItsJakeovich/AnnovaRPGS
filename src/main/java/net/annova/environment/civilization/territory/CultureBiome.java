package net.annova.environment.civilization.territory;

public enum CultureBiome {
    // Ocean, 20
    OCEAN(chunkType.OCEAN, 15, 5f),
    COLD_OCEAN(chunkType.OCEAN, 5, 15),
    LUKEWARM_OCEAN(chunkType.OCEAN, 10, 10),
    FROZEN_OCEAN(chunkType.OCEAN, 0, 20),
    WARM_OCEAN(chunkType.OCEAN, 20, 0),
    // 15
    DEEP_OCEAN(chunkType.OCEAN, 10, 2.5f),
    DEEP_COLD_OCEAN(chunkType.OCEAN, 2.5f, 10),
    DEEP_LUKEWARM_OCEAN(chunkType.OCEAN, 6.25f, 6.25f),
    DEEP_FROZEN_OCEAN(chunkType.OCEAN, 0, 12.5f),

    // rivers, 35
    FROZEN_RIVER(chunkType.RIVER, 15, 20),
    RIVER(chunkType.RIVER,30, 5),

    // swamp, 200
    SWAMP(chunkType.SWAMP, 150, 50),
    MANGROVE_SWAMP(chunkType.SWAMP, 75, 125),

    // plains, 250
    PLAINS(chunkType.PLAINS, 200, 50),
    SNOWY_PLAINS(chunkType.PLAINS, 50, 200), // snowy
    SUNFLOWER_PLAINS(chunkType.PLAINS, 225, 25),
    ICE_SPIKES(chunkType.PLAINS, 20, 230), // hills
    MEADOW(chunkType.PLAINS, 120, 130), // hills

    // forests, 250
    FOREST(chunkType.FOREST, 150f, 100f),
    BIRCH_FOREST(chunkType.FOREST, 175, 75),
    DARK_FOREST(chunkType.FOREST, 125, 125),
    FLOWER_FOREST(chunkType.FOREST, 200, 50),
    OLD_GROWTH_BIRCH_FOREST(chunkType.FOREST, 100, 150), // hills
    GROVE(chunkType.FOREST, 25, 225), // snowy hills
    CHERRY_GROVE(chunkType.FOREST, 150, 100), // hills

    // jungle 225
    JUNGLE(chunkType.JUNGLE, 120, 105),
    SPARSE_JUNGLE(chunkType.JUNGLE, 175, 50),
    BAMBOO_JUNGLE(chunkType.JUNGLE, 75, 150),

    // taiga 200
    TAIGA(chunkType.TAIGA, 125f, 75f),
    SNOWY_TAIGA(chunkType.TAIGA, 75, 125), // snowy
    OLD_GROWTH_PINE_TAIGA(chunkType.TAIGA, 50, 150), // hills
    OLD_GROWTH_SPRUCE_TAIGA(chunkType.TAIGA, 35, 165), // hills

    // coasts, 50
    BEACH(chunkType.COAST, 35, 15),
    SNOWY_BEACH(chunkType.COAST, 10, 40),
    STONY_SHORE(chunkType.COAST, 25, 25),

    // desert, 200
    DESERT(chunkType.DESERT, 50f, 150f),

    // savanna 225
    SAVANNA(chunkType.SAVANNA, 175, 50),
    SAVANNA_PLATEAU(chunkType.SAVANNA, 100, 125), // hills

    // mesa 200
    BADLANDS(chunkType.MESA, 75, 125),
    WOODED_BADLANDS(chunkType.MESA, 35, 165), // forest
    ERODED_BADLANDS(chunkType.MESA, 50, 150), // spikes

    // hills 200
    WINDSWEPT_HILLS(chunkType.HILLS, 50, 150),
    WINDSWEPT_GRAVELLY_HILLS(chunkType.HILLS, 35, 165),
    WINDSWEPT_FOREST(chunkType.HILLS, 100, 100), // forest
    WINDSWEPT_SAVANNA(chunkType.HILLS, 15, 185), // savanna

    // Mountains, 100
    FROZEN_PEAKS(chunkType.MOUNTAIN, 5, 95), // snowy
    JAGGED_PEAKS(chunkType.MOUNTAIN, 22.5f, 77.5f),
    STONY_PEAKS(chunkType.MOUNTAIN, 35, 65),
    SNOWY_SLOPES(chunkType.MOUNTAIN, 10, 90), // snowy

    // unique, 300
    MUSHROOM_FIELDS(chunkType.UNIQUE, 175, 125),

    // cave
    DEEP_DARK(chunkType.CAVE, 0, 0),
    DRIPSTONE_CAVES(chunkType.CAVE, 0, 0),
    LUSH_CAVES(chunkType.CAVE, 0, 0),

    // unused
    NETHER_WASTES(chunkType.UNKNOWN, 0, 0),
    THE_END(chunkType.UNKNOWN, 0, 0),
    SMALL_END_ISLANDS(chunkType.UNKNOWN, 0, 0),
    END_MIDLANDS(chunkType.UNKNOWN, 0, 0),
    END_HIGHLANDS(chunkType.UNKNOWN, 0, 0),
    END_BARRENS(chunkType.UNKNOWN, 0, 0),
    SOUL_SAND_VALLEY(chunkType.UNKNOWN, 0, 0),
    BASALT_DELTAS(chunkType.UNKNOWN, 0, 0),
    CRIMSON_FOREST(chunkType.UNKNOWN, 0, 0),
    WARPED_FOREST(chunkType.UNKNOWN, 0, 0),
    THE_VOID(chunkType.UNKNOWN, 0, 0),
    ;

    private final float growth;
    private final float hammers;
    private final chunkType type;

    CultureBiome(float growth, float hammers) {
        this(chunkType.UNKNOWN, growth, hammers);
    }
    CultureBiome(chunkType type, float growth, float hammers) {
        this.type = type;
        this.growth = 10;
        this.hammers = 10;
    }

    public enum chunkType {
        OCEAN(110f),
        RIVER(110f),
        SWAMP(70f),
        PLAINS(140f),
        FOREST(120f),
        JUNGLE(105f),
        TAIGA(110f),
        COAST(110f),
        DESERT(80f),
        SAVANNA(125f),
        MESA(95f),
        HILLS(55f),
        MOUNTAIN(80f),
        UNIQUE(155f),
        CAVE(115f),
        UNKNOWN(10f)
        ;
        private final float movement; // в множителе

        chunkType(float movement) {
            this.movement = movement;
        }

        public float getMovement() {
            return movement;
        }
    }

    public float getGrowth() {
        return growth;
    }

    public float getHammers() {
        return hammers;
    }

    public float getHousing() {
        return 2.5f;
    }

    public chunkType getType() {
        return type;
    }
}
