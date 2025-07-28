package net.annova.environment.civilization;

import net.annova.object.person.RPGPerson;
import net.minecraft.core.Vec3i;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Town {
    private final String name;
    private final Civilization civilization;
    private final boolean capital;
    private final RPGPerson leader;
    private final List<TownBuild> builds = new ArrayList<>();
    private float growth = 0;
    private float hammers = 0;
    private float culture = 0;
    private float science = 0;
    private float population = 1;
    private float housing;
    private final List<AreaChunk> areas = new ArrayList<>();
    private final List<CultureChunk> chunks = new ArrayList<>();
    private final List<RPGPerson> members = new ArrayList<>();


    public Town(String name, RPGPerson leader, TownBuild hall, boolean capital, Civilization civilization) {
        this.name = name;
        this.leader = leader;
        this.members.add(leader);
        this.addBuild(hall);

        this.capital = capital;

        if (civilization == null) this.civilization = addCivilization(name, leader);
        else this.civilization = civilization;
        this.civilization.getTowns().add(this);

        TownCreateCE e = new TownCreateCE(this);
        Bukkit.getPluginManager().callEvent(e);
    }
    public Town(String name, RPGPerson leader, TownBuild hall) {
        this(name, leader, hall, true, null);

    }
    public Town(String name, RPGPerson leader, TownBuild hall, Civilization civilization) {
        this(name, leader, hall, false, civilization);
    }

    public String getName() {
        return name;
    }

    public boolean isCapital() {
        return capital;
    }

    public Civilization getCivilization() {
        return civilization;
    }

    public RPGPerson getLeader() {
        return leader;
    }

    public List<TownBuild> getBuilds() {
        return builds;
    }
    @Nullable
    public TownBuild getBuild(Vec3i position) {
        for (TownBuild build : this.getBuilds()) {
            if (build.getConstruction().getBuildPoint().equals(position)) return build;
        }
        return null;
    }
    public void addBuild(TownBuild build) {
        this.builds.add(build);
    }

    public void updateStats() {
        this.updateGrowth();
        this.updateHammers();
        this.updateHousing();
    }
    public void updateGrowth() {
        float growth = 0;
        for (AreaChunk area : this.getAreas()) {
            growth+=area.getBiome().getGrowth();
        }
        this.growth = growth == 0 ? 5 : growth;
    }
    public void updateHammers() {
        float hammers = 0;
        for (AreaChunk area : this.getAreas()) {
            hammers+=area.getBiome().getHammers();
        }
        this.hammers = hammers == 0 ? 5 : hammers;
    }
    public void updateHousing() {
        float housing = 1f;
        for (AreaChunk area : this.getAreas()) {
            housing+=area.getBiome().getHousing();
        }
        this.housing = housing - population;
    }

    public float getGrowth() {
        return growth;
    }

    public void setGrowth(float growth) {
        this.growth = growth;
    }

    public float getHammers() {
        return hammers;
    }

    public void setHammers(float hammers) {
        this.hammers = hammers;
    }

    public float getCulture() {
        return culture;
    }

    public void setCulture(float culture) {
        this.culture = culture;
    }

    public float getScience() {
        return science;
    }

    public void setScience(float science) {
        this.science = science;
    }

    public float getPopulation() {
        return population;
    }

    public void setPopulation(float population) {
        this.population = population;
    }

    public float getHousing() {
        return housing;
    }

    public void setHousing(float housing) {
        this.housing = housing;
    }

    public List<CultureChunk> getChunks() {
        return chunks;
    }
    public void addChunk(CultureChunk chunk) {
        chunks.add(chunk);
        addTownChunk(chunk);
    }

    public List<AreaChunk> getAreas() {
        return areas;
    }
    public void addArea(AreaChunk areaChunk) {
        areas.add(areaChunk);
        this.updateStats();
    }

    public List<RPGPerson> getMembers() {
        return members;
    }
}
