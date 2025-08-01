package net.annova.command.cmdCivilization;

import net.annova.command.register.CommandArg;
import net.annova.environment.civilization.Civilization;
import net.annova.environment.civilization.ResearchManager;
import net.annova.environment.civilization.Town;
import net.annova.environment.civilization.tech.AnnovaTech;
import net.annova.lang.AISString;
import net.annova.object.Person;
import net.annova.object.person.RPGPerson;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.StringJoiner;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getCivilization;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class CivilizationCode {
    @CommandArg(name = "info", aliases = {"i", "и", "штащ", "инфо", "byaj"})
    public void info(Player player, String[] args) {
        RPGPerson rpg = getRPGPerson(player);
        Person person = rpg.getPerson();
        Civilization civilization;
        if (args.length == 1) {
            Town town = rpg.getTown();
            if (town == null) {
                person.send(getInstance(), "civ-info-cancel-town");
                return;
            }
            civilization = town.getCivilization();
        } else {
            if ((civilization = getCivilization(args[1])) == null) {
                person.send(getInstance(), "civ-info-not-found", "{name}", args[1]);
                return;
            }
        }

        StringJoiner leaders = new StringJoiner("{sc},{mc}");
        for (RPGPerson pers : civilization.getLeaders()) leaders.add(pers.getPerson().getName());
        AISString info = person.getAISString(getInstance(), "civ-info")
                .replaceTag("{name}", civilization.getName())
                .replaceTag("{stage}", civilization.getStage().toString(person))
                .replaceTag("{leaders}", leaders + "{sc}.")
                ;


        AISString townString = new AISString("");
        Set<Town> towns = civilization.getTowns();
        if (!civilization.getLeaders().contains(rpg)) {
            for (Town town : towns) townString.append("{}{pc}  ◆ {mc}" + town.getName());
        } else { // Если лидер этой цивилизации
            // Towns
            for (Town town : towns) townString.append("{}{pc}  ◆ {mc}" + town.toString());
            // Researchs
            ResearchManager manager = civilization.getResearchManager();
            AISString researchString = new AISString("{}{pc}  ◆ {sc}")
                    .append(manager.researchToString(rpg, AnnovaTech.Type.CULTURE))
                    .append("{}{pc}  ◆ {sc}")
                    .append(manager.researchToString(rpg, AnnovaTech.Type.COMMERCIAL))
                    .append("{}{pc}  ◆ {sc}")
                    .append(manager.researchToString(rpg, AnnovaTech.Type.MILITARY));
            info.append(
                    person.getAISString(getInstance(), "civ-info-advanced")
                            .replaceTag("{science}", (int)civilization.getScience() + "{b}\uD83D\uDCD6")
                            .replaceTag("{research}", researchString)
            );
        }
        person.send(info
                .replaceTag("{towns}", townString)
        );
    }
}
