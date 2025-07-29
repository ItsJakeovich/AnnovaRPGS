package net.annova.environment.civilization.territory;

public enum CultureBiome {
    // Ocean, 20
    OCEAN(Type.OCEAN, 10, 10),
    COLD_OCEAN(Type.OCEAN, 10, 10),
    LUKEWARM_OCEAN(Type.OCEAN, 10, 10),
    FROZEN_OCEAN(Type.OCEAN, 10, 10),
    WARM_OCEAN(Type.OCEAN, 10, 10),
    DEEP_OCEAN(Type.OCEAN, 10, 10),
    DEEP_COLD_OCEAN(Type.OCEAN,10, 10),
    DEEP_LUKEWARM_OCEAN(Type.OCEAN, 10, 10),
    DEEP_FROZEN_OCEAN(Type.OCEAN, 10, 10),

    // rivers, 35
    FROZEN_RIVER(Type.RIVER, 10, 10),
    RIVER(Type.RIVER,10, 10),

    // swamp, 200
    SWAMP(Type.SWAMP, 10, 10),
    MANGROVE_SWAMP(Type.SWAMP, 10, 10),

    // plains, 250
    PLAINS(Type.PLAINS, 10, 10),
    SNOWY_PLAINS(Type.PLAINS, 10, 10), // snowy
    SUNFLOWER_PLAINS(Type.PLAINS, 10, 10),
    ICE_SPIKES(Type.PLAINS, 10, 10), // hills
    MEADOW(Type.PLAINS, 10, 10), // hills

    // forests, 250
    FOREST(Type.FOREST, 10, 10),
    BIRCH_FOREST(Type.FOREST, 10, 10),
    DARK_FOREST(Type.FOREST, 10, 10),
    FLOWER_FOREST(Type.FOREST, 10, 10),
    OLD_GROWTH_BIRCH_FOREST(Type.FOREST, 10, 10), // hills
    GROVE(Type.FOREST, 10, 10), // snowy hills
    CHERRY_GROVE(Type.FOREST, 10, 10), // hills

    // jungle 225
    JUNGLE(Type.JUNGLE, 10, 10),
    SPARSE_JUNGLE(Type.JUNGLE, 10, 10),
    BAMBOO_JUNGLE(Type.JUNGLE, 10, 10),

    // taiga 200
    TAIGA(Type.TAIGA, 10, 10),
    SNOWY_TAIGA(Type.TAIGA, 10, 10), // snowy
    OLD_GROWTH_PINE_TAIGA(Type.TAIGA, 10, 10), // hills
    OLD_GROWTH_SPRUCE_TAIGA(Type.TAIGA, 10, 10), // hills

    // coasts, 50
    BEACH(Type.COAST, 10, 10),
    SNOWY_BEACH(Type.COAST, 10, 10),
    STONY_SHORE(Type.COAST, 10, 10),

    // desert, 200
    DESERT(Type.DESERT, 10, 10),

    // savanna 225
    SAVANNA(Type.SAVANNA, 10, 10),
    SAVANNA_PLATEAU(Type.SAVANNA, 10, 10), // hills

    // mesa 200
    BADLANDS(Type.MESA, 10, 10),
    WOODED_BADLANDS(Type.MESA, 10, 10), // forest
    ERODED_BADLANDS(Type.MESA, 10, 10), // spikes

    // hills 200
    WINDSWEPT_HILLS(Type.HILLS, 10, 10),
    WINDSWEPT_GRAVELLY_HILLS(Type.HILLS, 10, 10),
    WINDSWEPT_FOREST(Type.HILLS, 10, 10), // forest
    WINDSWEPT_SAVANNA(Type.HILLS, 10, 10), // savanna

    // Mountains, 100
    FROZEN_PEAKS(Type.MOUNTAIN, 10, 10), // snowy
    JAGGED_PEAKS(Type.MOUNTAIN, 10, 10),
    STONY_PEAKS(Type.MOUNTAIN, 10, 10),
    SNOWY_SLOPES(Type.MOUNTAIN, 10, 10), // snowy

    // unique, 300
    MUSHROOM_FIELDS(Type.UNIQUE, 10, 10),

    // cave
    DEEP_DARK(Type.CAVE, 10, 10),
    DRIPSTONE_CAVES(Type.CAVE, 10, 10),
    LUSH_CAVES(Type.CAVE, 10, 10),

    // unused
    NETHER_WASTES(Type.EMPTY, 0, 0),
    THE_END(Type.EMPTY, 0, 0),
    SMALL_END_ISLANDS(Type.EMPTY, 0, 0),
    END_MIDLANDS(Type.EMPTY, 0, 0),
    END_HIGHLANDS(Type.EMPTY, 0, 0),
    END_BARRENS(Type.EMPTY, 0, 0),
    SOUL_SAND_VALLEY(Type.EMPTY, 0, 0),
    BASALT_DELTAS(Type.EMPTY, 0, 0),
    CRIMSON_FOREST(Type.EMPTY, 0, 0),
    WARPED_FOREST(Type.EMPTY, 0, 0),
    THE_VOID(Type.EMPTY, 0, 0),
    ;

    private final CultureBiome.Type type;
    private final float growth, hammer;

    CultureBiome(CultureBiome.Type type, float growth, float hammer) {
        this.type = type;
        this.growth = growth;
        this.hammer = hammer;
    }

    public Type getType() {
        return type;
    }
    public float getGrowth() {
        return growth;
    }
    public float getHammers() {
        return hammer;
    }

    public enum Type {
        OCEAN(),
        RIVER(),
        SWAMP(),
        PLAINS(),
        FOREST(),
        JUNGLE(),
        TAIGA(),
        COAST(),
        DESERT(),
        SAVANNA(),
        MESA(),
        HILLS(),
        MOUNTAIN(),
        UNIQUE(),
        CAVE(),
        EMPTY()
    }
}