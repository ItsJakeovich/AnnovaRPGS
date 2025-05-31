package net.annova.object.entity.core;

import net.annova.object.entity.mobs.Boar;
import net.annova.object.entity.mobs.Snake;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.Set;

import static net.annova.environment.world.Location.getChorusLocation;
import static net.annova.util.BukkitObjects.chance;

public enum RPGSpawning { // UpdateSpawnTimer
    SNAKE(new Snake(), 0.05),
    BOAR(new Boar(), 0.05)
    ;

    private final RPGEntity entity;
    private final World world;
    private final double chance;
    private final Set<Biome> biomes;
    RPGSpawning(RPGEntity entity, double chance, Biome... biomes) {
        this(entity, "", chance, biomes);
    }
    RPGSpawning(RPGEntity entity, String world, double chance, Biome... biomes) {
        this.entity = entity;
        this.world = Bukkit.getWorld(world);
        this.chance = chance;
        this.biomes = Set.of(biomes);
    }

    public RPGEntity getEntity() {
        return entity;
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

    public void trySpawn(Location location) {
        if (!chance(this.getChance())) return;
        Set<Biome> biomes = this.getBiomes();
        if (!biomes.isEmpty()) if (!biomes.contains(location.getBlock().getBiome())) return;
        World world = this.getWorld();
        World spawnWorld = location.getWorld();
        if (world != null) if (!world.equals(spawnWorld)) return;

        Location spawn = getChorusLocation(location, 15);
        if (spawn.getBlock().getLightLevel() > 7) return;
        this.getEntity().create(spawn);
    }
}
