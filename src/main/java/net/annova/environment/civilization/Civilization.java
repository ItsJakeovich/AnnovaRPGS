package net.annova.environment.civilization;

import net.annova.lang.Replacement;
import net.annova.main.GlobalRPG;
import net.annova.object.person.RPGPerson;

import java.util.*;

import static net.annova.main.AnnovaRPG.getInstance;

public class Civilization {
    private String name;
    private Stage stage;

    private final ResearchManager researchManager;

    private Town capital;
    private final Set<Town> towns = new HashSet<>();
    private final Set<RPGPerson> leaders = new HashSet<>();
    private final Set<RPGPerson> citizens = new HashSet<>();

    public Civilization(String name, RPGPerson leader) {
        this.name = name;
        this.leaders.add(leader);
        this.researchManager = new ResearchManager(this);
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
        GlobalRPG.addTown(town);
    }
    public Set<Town> getTowns() {
        return towns;
    }

    public float getScience() {
        float base = this.stage.baseScience;
        for (Town town : this.getTowns()) {
            base+=town.getScience();
        }
        return base;
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
    }
}
