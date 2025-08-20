package net.annova.environment.civilization;

import net.annova.environment.civilization.build.BuildBase;
import net.annova.environment.civilization.build.TownBuild;
import net.annova.environment.civilization.territory.AreaChunk;
import net.annova.environment.civilization.territory.CultureBiome;
import net.annova.lang.AISReplacement;
import net.annova.lang.PathReplacement;
import net.annova.lang.Replacement;
import net.annova.main.AnnovaAPI;
import net.annova.main.AnnovaSQL;
import net.annova.main.GlobalRPG;
import net.annova.object.SimpleConstruction;
import net.annova.object.SimpleStructure;
import net.annova.object.person.RPGPerson;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static net.annova.environment.world.GlobalWorld.getWorldSector;
import static net.annova.main.AnnovaAPI.getMinecraftServer;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.*;
import static net.annova.util.AISUtil.joinList;
import static net.annova.util.AISUtil.loadList;

public class Town {
    private String name;
    private Civilization civilization = null;
    private final List<TownBuild> constructions = new ArrayList<>();
    private final List<TownBuild> builds = new ArrayList<>();
    private final ServerLevel level;
    private final AreaChunk centerChunk;
    private final Set<AreaChunk> chunks = new HashSet<>();
    private final Set<RPGPerson> leaders = new HashSet<>();
    private final Set<RPGPerson> members = new HashSet<>();

    private boolean todayClaim = false;

    public static final float BASE_GROWTH = 5;
    public static final float BASE_HAMMERS = 5;
    private final EnumMap<StatType, EnumMap<StatSources, Float>> stats = new EnumMap<>(StatType.class);
    public enum StatSources {
        BASE,
        CHUNK
    }

    public static String TABLE = "TOWN";
    public static void init() {
        AnnovaSQL sql = AnnovaAPI.getSQL();
        sql.initTable(TABLE,
                "NAME VARCHAR(20)",
                "CONSTRUCTIONS MEDIUMTEXT",
                "BUILDS MEDIUMTEXT",
                "LEVEL VARCHAR(30)",
                "CENTER_CHUNK VARCHAR(60)",
                "CHUNKS TEXT",
                "LEADERS TEXT",
                "MEMBERS TEXT",

                "PRIMARY KEY (NAME)"
        );
        sql.loadFromDB(TABLE, Town.class);
    }
    public void save() {
        Map<String, Object> save = new HashMap<>();
        save.put("NAME", this.name);
        save.put("CONSTRUCTIONS", joinList(this.constructions, TownBuild::serialize, ">"));
        save.put("BUILDS", joinList(this.builds, TownBuild::serialize, ">"));
        save.put("LEVEL", this.level.serverLevelData.getLevelName());
        save.put("CENTER_CHUNK", this.centerChunk.serialize());
        Set<AreaChunk> chunksForDB = new HashSet<>(this.chunks);
        chunksForDB.remove(centerChunk);
        save.put("CHUNKS", joinList(chunksForDB, AreaChunk::serialize, ">"));
        save.put("LEADERS", joinList(this.leaders, p -> p.getPerson().getName(), ">"));
        save.put("MEMBERS", joinList(this.members, p -> p.getPerson().getName(), ">"));

        AnnovaAPI.getSQL().saveObject(TABLE, "NAME", save);
    }

    public Town(ResultSet rs) throws SQLException {
        this.name = rs.getString("NAME");
        // Цивилизация выставляется в цивилизации.
        loadList(this.constructions, rs.getString("CONSTRUCTIONS"), c -> new TownBuild(this, c), ">");
        loadList(this.builds, rs.getString("BUILDS"), b -> new TownBuild(this, b), ">");

        this.level = getMinecraftServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.withDefaultNamespace(rs.getString("LEVEL"))));

        this.centerChunk = new AreaChunk(this, rs.getString("CENTER_CHUNK")); this.addChunk(centerChunk);
        String chunks = rs.getString("CHUNKS");
        if (!chunks.isBlank()) for (String chunk : chunks.split(">")) {
            this.addChunk(new AreaChunk(this, chunk));
        }

        loadList(this.leaders, rs.getString("LEADERS"), GlobalRPG::getRPGPerson, ">");
        loadList(this.members, rs.getString("MEMBERS"), GlobalRPG::getRPGPerson, ">");

        this.initValues();

        addTown(this);
    }

    public Town(String name, AreaChunk center, RPGPerson leader) {
        this(name, leader, center, null);
    }
    public Town(String name, RPGPerson leader, AreaChunk center, Civilization civilization) {
        this.name = name;
        this.leaders.add(leader);
        this.members.add(leader);
        this.level = leader.getServerPlayer().serverLevel();
        this.centerChunk = center; this.addChunk(center);

        if (civilization == null) {
            this.civilization = addCivilization(name, leader);
            this.civilization.setCapital(this);
        } else this.civilization = civilization;
        this.civilization.addTown(this);

        // Update Statistics
        this.initValues();
    }

    public void initValues() {
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

    public List<TownBuild> getConstructions() {
        return constructions;
    }

    /**
     * Добавляет постройку в город, начинает ее строительство.
     * @param construction структура что будет строится
     */
    public void addConstruction(SimpleConstruction construction) {
        TownBuild build = new TownBuild(this, construction);
        this.constructions.add(build);
        this.send("build-set",
                new PathReplacement("{construction}", getInstance(), build.getBase().getLocal()),
                new AISReplacement("{cost}", String.valueOf(build.getCost()))
        );
        getWorldSector(construction.getLevel()).addBuild(build);
    }
    public void updateConstruction() {
        if (this.constructions.isEmpty()) return;

        TownBuild first = this.constructions.getFirst();
        if (first.nextTick(this.getValue(StatType.HAMMERS))) {
            this.addBuild(first);
            this.constructions.removeFirst();
        }
    }

    public List<TownBuild> getBuilds() {
        return builds;
    }
    public void addBuild(TownBuild build) {
        this.builds.add(build);
    }

    public List<TownBuild> getAllStructures() {
        List<TownBuild> builds = new ArrayList<>(this.builds.size() + this.constructions.size());
        builds.addAll(this.builds);
        builds.addAll(this.constructions);
        return builds;
    }
    public boolean intersects(Vec3i min, SimpleStructure structure) {
        Vec3i max = structure.getMaxPoint(min);
        for (TownBuild build : this.constructions)
            if (build.getConstruction().intersect(min, max)) return true;
        for (TownBuild build : this.builds)
            if (build.getConstruction().intersect(min, max)) return true;
        return false;
    }

    public ServerLevel getLevel() {
        return level;
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
        this.send("town-invite-success", new AISReplacement("{nick}", person.getPerson().getName()));
    }
    public void kickMember(RPGPerson person) {
        this.members.remove(person);
        this.civilization.send("town-kick-success", new AISReplacement("{nick}", person.getPerson().getName()));
    }

    public boolean isTodayClaim() {
        return todayClaim;
    }

    public void setTodayClaim(boolean todayClaim) {
        this.todayClaim = todayClaim;
    }

    public void send(String message, Replacement... replacements) {
        for (RPGPerson person : this.members) {
            person.getPerson().send(getInstance(), message, replacements);
        }
    }

    public EnumSet<BuildBase> getAvailableBuilds() {
        return EnumSet.allOf(BuildBase.class);
    }

    public String toString() {
        String color = this.isCapital() ? "{ac}" : "{mc}";
        return color + this.getName()
                +"{sc}: {g}\uD83C\uDF3F{mc}"+(int)this.getValue(StatType.GROWTH)
                + "{sc}, {o}\uD83D\uDEE0{mc}"+(int)this.getValue(StatType.HAMMERS)
                + "{sc}, {p}\uD83C\uDFBC{mc}"+(int)this.getValue(StatType.CULTURE)
                + "{sc}, {b}\uD83D\uDD0E{mc}"+ (int)this.getValue(StatType.SCIENCE);
    }
}
