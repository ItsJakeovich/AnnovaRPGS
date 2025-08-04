package net.annova.environment.civilization;

import net.annova.environment.civilization.territory.AreaChunk;
import net.annova.environment.civilization.territory.CultureBiome;
import net.annova.object.person.RPGPerson;
import net.minecraft.core.Vec3i;

import java.util.*;

import static net.annova.main.GlobalRPG.addCivilization;
import static net.annova.main.GlobalRPG.addAreaChunk;

public class Town {
    private String name;
    private Civilization civilization;
    private final Map<Vec3i, TownBuild> builds = new HashMap<>();
    private final AreaChunk centerChunk;
    private final Set<AreaChunk> chunks = new HashSet<>();
    private final Set<RPGPerson> leaders = new HashSet<>();
    private final Set<RPGPerson> members = new HashSet<>();

    public static final float BASE_GROWTH = 5;
    public static final float BASE_HAMMERS = 5;
    private final EnumMap<StatType, EnumMap<StatSources, Float>> stats = new EnumMap<>(StatType.class);
    public enum StatSources {
        BASE,
        CHUNK
    }

    public Town(String name, AreaChunk center, RPGPerson leader) {
        this(name, leader, center, null);
    }
    public Town(String name, RPGPerson leader, AreaChunk center, Civilization civilization) {
        this.name = name;
        this.leaders.add(leader);
        this.members.add(leader);
        this.centerChunk = center; this.addChunk(center); addAreaChunk(this, centerChunk);

        if (civilization == null) {
            this.civilization = addCivilization(name, leader);
            this.civilization.setCapital(this);
        } else this.civilization = civilization;
        this.civilization.addTown(this);

        // Update Statistics
        for (StatType type : StatType.values()) {
            this.stats.put(type, new EnumMap<>(StatSources.class));
            for (StatSources source : StatSources.values()) {
                this.stats.get(type).put(source, 0.0f);
            }
        }
        this.setValue(StatType.GROWTH, StatSources.BASE, BASE_GROWTH);
        this.setValue(StatType.HAMMERS, StatSources.BASE, BASE_HAMMERS);
        this.updateChunks();
    }

    public float getValue(StatType type) {
        float value = 0;
        EnumMap<StatSources, Float> stats = this.stats.get(type);
        for (StatSources source : StatSources.values()) {
            value += stats.get(source);
        }
        return value;
    }
    public float getStatSource(StatType type, StatSources source) {
        return this.stats.get(type).get(source);
    }
    public void setValue(StatType type, StatSources source, float value) {
        this.stats.get(type).put(source, value);
    }

    public void updateChunks() {
        float growth = 0, hammers = 0;
        for (AreaChunk area : this.chunks) { CultureBiome biome = area.getBiome();
            growth += biome.getGrowth();
            hammers += biome.getHammers();
        }
        this.setValue(StatType.GROWTH, StatSources.CHUNK, growth);
        this.setValue(StatType.HAMMERS, StatSources.CHUNK, hammers);
    }

    public String getName() {
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

    public AreaChunk getCenterChunk() {
        return centerChunk;
    }

    public Set<AreaChunk> getChunks() {
        return chunks;
    }
    public void addChunk(AreaChunk chunk) {
        this.chunks.add(chunk);
        addAreaChunk(this, chunk);
    }

    public Set<RPGPerson> getLeaders() {
        return leaders;
    }
    public void addLeaders(RPGPerson leader) {
        this.leaders.add(leader);
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
                +"{sc} - {g}\uD83C\uDF3F{mc}"+(int)this.getValue(StatType.GROWTH)
                + "{sc}, {o}\uD83D\uDEE0{mc}"+(int)this.getValue(StatType.HAMMERS)
                + "{sc}, {p}\uD83C\uDFBC{mc}"+(int)this.getValue(StatType.CULTURE)
                + "{sc}, {b}\uD83D\uDD0E{mc}"+ (int)this.getValue(StatType.SCIENCE);
    }
}
