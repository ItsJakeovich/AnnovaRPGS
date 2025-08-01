package net.annova.environment.civilization;

import net.annova.environment.civilization.territory.AreaChunk;
import net.annova.object.person.RPGPerson;
import net.minecraft.core.Vec3i;

import java.util.*;

import static net.annova.main.GlobalRPG.addCivilization;
import static net.annova.main.GlobalRPG.addTownChunk;

public class Town {
    private String name;
    private Civilization civilization;
    private final Map<Vec3i, TownBuild> builds = new HashMap<>();
    private float growth = 0, hammers = 0, culture = 0, science = 0;
    private final AreaChunk centerChunk;
    private final Set<AreaChunk> chunks = new HashSet<>();
    private RPGPerson leader;
    private final Set<RPGPerson> members = new HashSet<>();

    public Town(String name, AreaChunk center, RPGPerson leader) {
        this(name, leader, center, null);
    }
    public Town(String name, RPGPerson leader, AreaChunk center, Civilization civilization) {
        this.name = name;
        this.leader = leader;
        this.members.add(leader);
        this.centerChunk = center; addTownChunk(this, centerChunk);

        if (civilization == null) {
            this.civilization = addCivilization(name, leader);
            this.civilization.setCapital(this);
        } else this.civilization = civilization;
        this.civilization.addTown(this);
    }

    public String  getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Civilization getCivilization() {
        return civilization;
    }
    public void setCivilization(Civilization civilization) {
        this.civilization = civilization;
    }
    public boolean isCapital() {
        return this == civilization.getCapital();
    }

    public TownBuild getBuild(Vec3i position) {
        return this.builds.get(position);
    }
    public Collection<TownBuild> getBuilds() {
        return builds.values();
    }
    public void addBuild(TownBuild build) {
        this.builds.put(build.getConstruction().getBuildPoint(), build);
    }

    public void updateStats() {
        this.updateGrowth();
        this.updateHammers();
    }
    public void updateGrowth() {
        float growth = 0;
        for (AreaChunk area : this.getChunks())
            growth += area.getBiome().getGrowth();
        this.growth = growth == 0 ? 5 : growth;
    }
    public void updateHammers() {
        float hammers = 0;
        for (AreaChunk area : this.getChunks())
            hammers += area.getBiome().getHammers();
        this.hammers = hammers == 0 ? 5 : hammers;
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

    public AreaChunk getCenterChunk() {
        return centerChunk;
    }

    public Set<AreaChunk> getChunks() {
        return chunks;
    }
    public void addChunk(AreaChunk chunk) {
        this.chunks.add(chunk);
        addTownChunk(this, chunk);
    }

    public RPGPerson getLeader() {
        return leader;
    }
    public void setLeader(RPGPerson leader) {
        this.leader = leader;
    }

    public Set<RPGPerson> getMembers() {
        return members;
    }
    public void addMember(RPGPerson person) {
        this.members.add(person);
    }

    public String toString() {
        String color = this.isCapital() ? "{ac}" : "{mc}";
        return color + this.getName()
                +"{sc} - {g}\uD83C\uDF3F{mc}"+(int)this.getGrowth()
                + "{sc}, {o}\uD83D\uDEE0{mc}"+(int)this.getHammers()
                + "{sc}, {p}\uD83C\uDFBC{mc}"+(int)this.getCulture()
                + "{sc}, {b}\uD83D\uDD0E{mc}"+ (int)this.getScience();
    }
}
