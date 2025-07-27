package net.annova.environment.civilization;

import net.annova.environment.civilization.tech.AnnovaTech;
import net.annova.environment.civilization.tech.Research;
import net.annova.object.person.RPGPerson;
import net.annova.util.AISString;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static net.annova.environment.civilization.tech.AnnovaTech.Type.*;
import static net.annova.main.AnnovaRPG.getInstance;


public class Civilization {
    private final Component name;
    private Stage stage;

    private Research culture;
    private AnnovaTech cultureGoal;
    private Research commercial;
    private AnnovaTech commercialGoal;
    private Research military;
    private AnnovaTech militaryGoal;
    private final List<AnnovaTech> completeTechs = new ArrayList<>();

    private final List<Town> towns = new ArrayList<>();
    private final List<RPGPerson> leaders = new ArrayList<>();
    private final List<RPGPerson> citizens = new ArrayList<>();

    public Civilization(String name, RPGPerson leader) {
        this.name = Component.literal(name);
        this.leaders.add(leader);
    }

    public Component getName() {
        return name;
    }

    public Research getCultureResearch() {
        return culture;
    }
    public Research getCommercialResearch() {
        return commercial;
    }
    public Research getMilitaryResearch() {
        return military;
    }
    public Research getResearch(AnnovaTech.Type type) {
        return switch (type) {
            case CULTURE -> culture;
            case COMMERCIAL -> commercial;
            case MILITARY -> military;
        };
    }
    public void setResearch(AnnovaTech.Type type, Research tech) {
        switch (type) {
            case CULTURE -> culture = tech;
            case COMMERCIAL -> commercial = tech;
            case MILITARY -> military = tech;
        }
    }

    public AnnovaTech getGoal(AnnovaTech.Type type) {
        return switch (type) {
            case CULTURE -> cultureGoal;
            case COMMERCIAL -> commercialGoal;
            case MILITARY -> militaryGoal;
        };
    }
    public void setGoal(AnnovaTech.Type type, AnnovaTech tech) {
        switch (type) {
            case CULTURE -> this.cultureGoal = tech;
            case COMMERCIAL -> this.commercialGoal = tech;
            case MILITARY -> this.militaryGoal = tech;
        };
    }

    public void send(String message) {
        for (RPGPerson person : this.citizens) {
            person.getPerson().send(getInstance(), message);
        }
    }
    public void send(String message, String from, String to) {
        for (RPGPerson person : this.citizens) {
            person.getPerson().send(getInstance(), message, from, to);
        }
    }

    public List<AnnovaTech> getCompleteTechs() {
        return completeTechs;
    }

    /**
     * @param type is
     * @return true if civilization has a tech-type queue, false if not.
     */
    public boolean hasGoal(AnnovaTech.Type type) {
        return this.getGoal(type) != null;
    }

    public void startNextTechnology(AnnovaTech.Type type) {
        // Check: civ have goal or not. If not: returned the method and send message
        if (!this.hasGoal(type)) {
            this.send("tech-" + type.name().toLowerCase() + "-no-queue");
            return;
        }

        EnumSet<AnnovaTech> techs = EnumSet.noneOf(AnnovaTech.class);
        techs.add(this.getGoal(type));

        while (true) {
            for (AnnovaTech tech : techs) {
                EnumSet<AnnovaTech> neededTechs = EnumSet.noneOf(AnnovaTech.class);
                for (AnnovaTech depend : tech.getDepends()) {
                    if (completeTechs.contains(depend)) continue;
                    neededTechs.add(depend);
                }

                if (neededTechs.isEmpty()) {
                    this.setResearch(type, new Research(tech));
                    this.send("tech-start", "{tech}", tech.name());
                    return;
                }

                techs.clear();
                techs.addAll(neededTechs);
                /*// if list of technologies empty - return the method.
                if (techs.isEmpty()) {
                    send("tech-" + type.name().toLowerCase() + "-no-start");
                    return;
                }*/
            }
        }
    }


    public void updateResearchProgress() {
        List<Research> active = new ArrayList<>();
        for (AnnovaTech.Type type : AnnovaTech.Type.values()) {
            Research research = this.getResearch(type);
            if (research == null) {
                this.send("civ-tech-" + type.name() + "-idle"); // пишет что не стоит изучение
            } else active.add(research); // Добавляет в активные
        }
        if (active.isEmpty()) {
            this.send("civ-tech-idle");
            return;
        }

        float progress = this.getScience() / active.size(); // P = S * 1.0 / active;

        for (Research research : active) {
            research.addProgress(progress);

            this.send("civ-tech-progress");
            Нужно добавить дополнение к AISString, которое бы позволило сделать шаблон для заменений тэгов.
        }
    }

    public void addTechProgress(float progress, Research research, AnnovaTech.Type type) {

        research.setProgress((int) progress);

        this.send("tech-progress",
                new Replacement("{tech}", research.getTech().getId(), true),
                new Replacement("{percent}", String.valueOf(progress / research.getTech().getScience() * 100), false)
        );
    }

    public void checkTechProgress(AnnovaTech.Type type) {
        float progress = this.getScience();
        Research research = null;
        String goal = null;

        // Initialization the current checked tech and her GoalTech
        switch (type) {
            case CULTURE -> {
                research = this.getCultureResearch();
                goal = this.getCultureGoal();
            }
            case COMMERCIAL -> {
                research = this.getCommercialResearch();
                goal = this.getCommercialGoal();
            }
            case MILITARY -> {
                research = this.getMilitaryResearch();
                goal = this.getMilitaryGoal();
            }
        }
        // Added current research progress for variable
        progress += research.getProgress();

        // Check: Tech ended or no. If no returned the method and add This progress into tech
        if (progress < research.getTech().getScience()) {
            addTechProgress(progress, research, CULTURE);
            return;
        }

        // Send a message for Civ about ended research
        this.send("tech-complete", new Replacement("{tech}", research.getTech().getId(), true));

        // Added the researched technology to complete techs list
        getCompleteTechs().add(research.getTech().getId());

        // Check: Tech have next Goal or no. If research have goal, start next research
        if (research.getTech().getId().equals(goal)) {
            send("tech-" + type.name().toLowerCase() + "-no-queue");
            this.cancelResearch(type);
        } else {
            startNextTechnology(type);
        }
    }

    public void cancelResearch(AnnovaTech.Type type) {
        // Setted 
        switch (type) {
            case CULTURE -> {
                setCultureResearch(null);
                setCultureGoal("");
            }
            case COMMERCIAL -> {
                setCommercialResearch(null);
                setCommercialGoal("");
            }
            case MILITARY -> {
                setMilitaryResearch(null);
                setMilitaryGoal("");
            }
        }
    }

    public List<Town> getTowns() {
        return towns;
    }

    public float getScience() {
        float base = 50;
        for (Town town : getTowns()) {
            base+=town.getScience();
        }
        return base;
    }

    public List<RPGPerson> getLeaders() {
        return leaders;
    }

    public List<RPGPerson> getCitizens() {
        return citizens;
    }
}
