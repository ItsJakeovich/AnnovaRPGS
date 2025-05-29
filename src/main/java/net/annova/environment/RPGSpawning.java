package net.annova.environment;

import net.annova.entity.mobs.Snake;
import net.annova.entity.core.RPGEntity;
import net.annova.entity.mobs.ice.Charlie;
import net.annova.entity.mobs.ice.Rudi;
import net.annova.entity.mobs.ice.Symon;
import net.annova.entity.mobs.ice.Yuri;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.Set;

public enum RPGSpawning { // UpdateSpawnTimer
    CHARLIE(new Charlie(), "world", 0.2),
    RUDI(new Rudi(), "world", 0.2),
    SYMON(new Symon(), "world", 0.2),
    YURI(new Yuri(), "world", 0.2),

    SNAKE(new Snake(), 0.1)
    ;

    private final RPGEntity entity;
    private final World.Environment environment;
    private final World world;
    private final double chance;
    private final Set<Biome> biomes;
    RPGSpawning(RPGEntity entity, double chance, Biome... biomes) {
        this(entity, null, "", chance, biomes);
    }
    RPGSpawning(RPGEntity entity, String world, double chance, Biome... biomes) {
        this(entity, null, world, chance, biomes);
    }
    RPGSpawning(RPGEntity entity, World.Environment environment, double chance, Biome... biomes) {
        this(entity, environment, "", chance, biomes);
    }
    RPGSpawning(RPGEntity entity, World.Environment environment, String world, double chance, Biome... biomes) {
        this.entity = entity;
        this.environment = environment;
        this.world = Bukkit.getWorld(world);
        this.chance = chance;
        this.biomes = Set.of(biomes);
    }

    public RPGEntity getEntity() {
        return entity;
    }
    public World.Environment getEnvironment() {
        return environment;
    }
    public World getWorld() {
        return world;
    }
    public double getChance() {
        return chance;
    }
    public Set<Biome> getBiomes() {
        return biomes;
    }
}
