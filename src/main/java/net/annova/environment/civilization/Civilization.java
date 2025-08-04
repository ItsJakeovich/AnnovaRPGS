package net.annova.environment.civilization;

import net.annova.lang.AISString;
import net.annova.lang.Replacement;
import net.annova.main.GlobalRPG;
import net.annova.object.Person;
import net.annova.object.person.RPGPerson;

import java.util.*;

import static net.annova.main.AnnovaRPG.getInstance;

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

    public Civilization(String name, RPGPerson leader) {
        this.name = name;
        this.stage = Stage.CAMP;
        this.leaders.add(leader);
        this.researchManager = new ResearchManager(this);
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
