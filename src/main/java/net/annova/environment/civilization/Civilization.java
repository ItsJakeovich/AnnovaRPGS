package net.annova.environment.civilization;

import net.annova.lang.AISString;
import net.annova.lang.Replacement;
import net.annova.main.AnnovaAPI;
import net.annova.main.AnnovaSQL;
import net.annova.main.GlobalRPG;
import net.annova.object.Person;
import net.annova.object.person.RPGPerson;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.*;

public class Civilization {
    private String name;
    private Stage stage;

    private final ResearchManager researchManager;
    private final EnumMap<StatSources, Float> science = new EnumMap<>(StatSources.class);
    public enum StatSources {
        BASE,
        TOWNS,
    }

    private Town capital;
    private final Set<Town> towns = new HashSet<>();
    private final Set<RPGPerson> leaders = new HashSet<>();
    private final Set<RPGPerson> citizens = new HashSet<>();

    public static final String TABLE = "CIVILIZATION";

    public static void init() {
        AnnovaSQL sql = AnnovaAPI.getSQL();
        sql.initTable(TABLE,
                "NAME VARCHAR(20)",
                "STAGE VARCHAR(12)",
                "RESEARCH_MANAGER MEDIUMTEXT",
                "CAPITAL VARCHAR(20)",
                "TOWNS TEXT",
                "LEADERS TEXT",
                "CITIZENS TEXT",

                "PRIMARY KEY (NAME)"
        );
        sql.loadFromDB(TABLE, Civilization.class);
    }
    public void save() {
        Map<String, Object> save = new HashMap<>();
        save.put("NAME", this.name);
        save.put("STAGE", this.stage.name());
        save.put("RESEARCH_MANAGER", this.researchManager.serialize());
        save.put("CAPITAL", this.capital.getName());

        StringJoiner joiner = new StringJoiner(",");
        for (Town town : this.towns) joiner.add(town.getName());
        save.put("TOWNS", joiner.toString()); joiner = new StringJoiner(",");
        for (RPGPerson leader : this.leaders) joiner.add(leader.getPerson().getName());
        save.put("LEADERS", joiner.toString()); joiner = new StringJoiner(",");
        for (RPGPerson citizen : this.citizens) joiner.add(citizen.getPerson().getName());
        save.put("CITIZENS", joiner.toString());

        AnnovaAPI.getSQL().saveObject(TABLE, "NAME", save);
    }

    public Civilization(ResultSet rs) throws SQLException {
        this.name = rs.getString("NAME");
        this.stage = Stage.valueOf(rs.getString("STAGE"));
        this.researchManager = ResearchManager.deserialize(this, rs.getString("RESEARCH_MANAGER"));
        this.capital = getTown(rs.getString("CAPITAL"));
        for (String town : rs.getString("TOWNS").split(","))
            this.towns.add(getTown(town));

        for (String leader : rs.getString("LEADERS").split(","))
            this.leaders.add(getRPGPerson(leader));
        for (String citizen : rs.getString("CITIZENS").split(","))
            this.citizens.add(getRPGPerson(citizen));

        this.initScience();
        addCivilization(this);
    }

    public Civilization(String name, RPGPerson leader) {
        this.name = name;
        this.stage = Stage.CAMP;
        this.leaders.add(leader);
        this.researchManager = new ResearchManager(this);
        this.initScience();
    }

    public void initScience() {
        for (StatSources source : StatSources.values()) {
            this.science.put(source, 0.0f);
        }
        this.setScienceSource(StatSources.BASE, this.stage.baseScience);
    }
    public float getScience() {
        float value = 0;
        for (StatSources source : StatSources.values()) {
            value += science.get(source);
        }
        return value;
    }
    public float getScienceSource(StatSources source) {
        return this.science.get(source);
    }
    public void setScienceSource(StatSources source, float value) {
        this.science.put(source, value);
    }

    public void updateTowns() {
        float science = 0;
        for (Town town : this.towns) science += town.getValue(StatType.SCIENCE);
        this.setScienceSource(StatSources.TOWNS, science);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ResearchManager getResearchManager() {
        return researchManager;
    }

    public void send(String message, Replacement... replacements) {
        for (RPGPerson person : this.citizens) {
            person.getPerson().send(getInstance(), message, replacements);
        }
    }
    public void send(String message, String from, String to) {
        for (RPGPerson person : this.citizens) {
            person.getPerson().send(getInstance(), message, from, to);
        }
    }

    public Town getCapital() {
        return capital;
    }
    public void setCapital(Town capital) {
        this.capital = capital;
    }

    public void addTown(Town town) {
        this.towns.add(town);
        this.citizens.addAll(town.getMembers());
        GlobalRPG.addTown(town);
    }
    public Set<Town> getTowns() {
        return towns;
    }

    public Set<RPGPerson> getLeaders() {
        return leaders;
    }

    public Set<RPGPerson> getCitizens() {
        return citizens;
    }

    public enum Stage {
        CAMP(10),
        PROVINCE(30),
        CIVILIZATION(80);

        private final float baseScience;
        Stage(float baseScience) {
            this.baseScience = baseScience;
        }

        public float getBaseScience() {
            return baseScience;
        }

        public AISString toString(Person person) {
            return person.getAISString(getInstance(), "stage-" + this.name());
        }
    }

    public String toString() {
        return this.getName()+"{sc} - {mc}"+this.getScience()+"{b}\uD83D\uDCD6";
    }
}
