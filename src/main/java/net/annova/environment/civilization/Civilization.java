package net.annova.environment.civilization;

import net.annova.environment.civilization.tech.AnnovaTech;
import net.annova.environment.civilization.tech.Research;
import net.annova.lang.AISReplacement;
import net.annova.lang.PathReplacement;
import net.annova.lang.Replacement;
import net.annova.object.person.RPGPerson;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static net.annova.environment.civilization.tech.AnnovaTech.Category.getByStageAndType;
import static net.annova.main.AnnovaRPG.getInstance;


public class Civilization {
    private final Component name;
    private Stage stage;

    private Research culture, commercial, military;
    private AnnovaTech cultureGoal, commercialGoal, militaryGoal;
    private final EnumSet<AnnovaTech> completeTechs = EnumSet.noneOf(AnnovaTech.class);

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

    public Stage getStage() {
        return stage;
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

    public EnumSet<AnnovaTech> getCompleteTechs() {
        return completeTechs;
    }

    /**
     * @param type is
     * @return true if civilization has a tech-type queue, false if not.
     */
    public boolean hasGoal(AnnovaTech.Type type) {
        return this.getGoal(type) != null;
    }

    public void updateResearchProgress() {
        List<Research> active = new ArrayList<>();
        for (AnnovaTech.Type type : AnnovaTech.Type.values()) {
            Research research = this.getResearch(type);
            if (research == null) continue;
            active.add(research); // Добавляет в активные
        }
        if (active.isEmpty()) {
            this.send("civ-research-" + this.stage.name() + "-idle");
            return;
        }

        float progress = this.getScience() / active.size(); // P = S*(1.0/active);

        for (Research research : active) {
            float cost = this.getResearchCost(research);
            float finalProgress = research.addProgress(progress);

            if (finalProgress < cost) { // ❖ Технология ⌈Мореходство⌋ изучена на 87% (12320/18000)!
                this.send("civ-tech-progress",
                        new PathReplacement("{tech}", getInstance(), research.getTech().name()),
                        new AISReplacement("{progress}", (int) (cost / finalProgress * 100)
                                + "{sc}% ("+finalProgress+"/"+cost+")")
                );
            } else { // ❖ Технология ⌈Мореходство⌋ была изучена!
                this.send("civ-research-complete",
                        new PathReplacement("{tech}", getInstance(), research.getTech().name())
                );
                this.completeResearch(research);
            }
        }
    }
    public void completeResearch(Research research) {
        AnnovaTech tech = research.getTech();
        AnnovaTech.Type type = tech.getType();

        this.completeTechs.add(tech);
        this.setResearch(type, null);
        if (tech == this.getGoal(type)) this.setGoal(type, null);
        this.nextResearchs();
    }
    public void nextResearchs() {
        for (AnnovaTech.Type type : AnnovaTech.Type.values()) {
            AnnovaTech goal = this.getGoal(type);
            if (goal == null) {
                this.send("civ-research-" + getByStageAndType(this.stage, type).name() + "-no-goal");
                continue;
            }

            AnnovaTech next = this.getNextTechnology(goal);
            if (next == null) continue;

            this.setResearch(next.getType(), new Research(next));
            this.send("civ-research-start", new PathReplacement("{tech}", getInstance(), next.name()));
        }
    }

    /**
     * @param goal is a target technology
     * @return null - queue is busy. AnnovaTech from line.
     */
    public AnnovaTech getNextTechnology(AnnovaTech goal) {
        EnumSet<AnnovaTech> techs = EnumSet.noneOf(AnnovaTech.class);
        techs.add(goal);

        while (!techs.isEmpty()) {
            for (AnnovaTech tech : techs) {
                EnumSet<AnnovaTech> neededTechs = EnumSet.noneOf(AnnovaTech.class);
                for (AnnovaTech depend : tech.getDepends()) {
                    if (completeTechs.contains(depend)) continue;
                    neededTechs.add(depend);
                }

                if (neededTechs.isEmpty() && this.getResearch(tech.getType()) == null)
                    return tech;

                techs.clear();
                techs.addAll(neededTechs);
            }
        }
        return null;
    }
    
    public float getResearchCost(Research research) {
        return research == null ? 0 : research.getTech().getBaseCost();
    }

    public List<Town> getTowns() {
        return towns;
    }

    public float getScience() {
        float base = this.stage.baseScience;
        for (Town town : this.getTowns()) {
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
