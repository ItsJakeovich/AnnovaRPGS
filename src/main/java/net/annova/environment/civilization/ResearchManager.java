package net.annova.environment.civilization;

import net.annova.environment.civilization.tech.AnnovaTech;
import net.annova.environment.civilization.tech.Research;
import net.annova.lang.AISReplacement;
import net.annova.lang.AISString;
import net.annova.lang.PathReplacement;
import net.annova.object.Person;
import net.annova.object.person.RPGPerson;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static net.annova.environment.civilization.tech.AnnovaTech.Category.getByStageAndType;
import static net.annova.main.AnnovaRPG.getInstance;

public class ResearchManager {
    private final Civilization civilization;

    private Research culture, commercial, military;
    private AnnovaTech cultureGoal, commercialGoal, militaryGoal;
    private final EnumSet<AnnovaTech> completeTechs = EnumSet.noneOf(AnnovaTech.class);

    public ResearchManager(Civilization civilization) {
        this.civilization = civilization;
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

    public AnnovaTech getMilitaryGoal() {
        return militaryGoal;
    }
    public AnnovaTech getCommercialGoal() {
        return commercialGoal;
    }
    public AnnovaTech getCultureGoal() {
        return cultureGoal;
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
            civilization.send("civ-research-" + civilization.getStage().name() + "-idle");
            return;
        }

        float progress = civilization.getScience() / active.size(); // P = S*(1.0/active);

        for (Research research : active) {
            float cost = this.getResearchCost(research);
            float finalProgress = research.addProgress(progress);

            if (finalProgress < cost) { // ❖ Технология ⌈Мореходство⌋ изучена на 87% (12320/18000)!
                civilization.send("civ-tech-progress",
                        new PathReplacement("{tech}", getInstance(), research.getTech().name()),
                        new AISReplacement("{progress}", (int) (cost / finalProgress * 100)
                                + "{sc}% ("+finalProgress+"/"+cost+")")
                );
            } else { // ❖ Технология ⌈Мореходство⌋ была изучена!
                civilization.send("civ-research-complete",
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
                civilization.send("civ-research-" + getByStageAndType(civilization.getStage(), type).name() + "-no-goal");
                continue;
            }

            AnnovaTech next = this.getNextTechnology(goal);
            if (next == null) continue;

            this.setResearch(next.getType(), new Research(next));
            civilization.send("civ-research-start", new PathReplacement("{tech}", getInstance(), next.name()));
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

    public AISString researchToString(RPGPerson rpgPerson, AnnovaTech.Type type) {
        Person person = rpgPerson.getPerson();
        AISString string = new AISString(person.getAISString(getInstance(), getByStageAndType(civilization.getStage(), type).name()) + "{sc}: {mc}");
        Research research = this.getResearch(type);
        if (research == null) {
            string.append(
                    research.getTech().toString(rpgPerson) + "{sc}" + (int)(this.getResearchCost(research)/research.getProgress()) + "%" +
                    "{sc}→ {mc}" + this.getCultureGoal().toString(rpgPerson) + "{sc}."
            );
        } else string.append(person.getAISString(getInstance(), "nothing") + "{sc}.");
        return string;
    }
}
