package net.annova.command.cmdCivilization;

import net.annova.command.register.CommandArg;
import net.annova.environment.civilization.Civilization;
import net.annova.environment.civilization.Town;
import net.annova.lang.AISString;
import net.annova.object.Person;
import net.annova.object.person.RPGPerson;
import org.bukkit.entity.Player;

import java.util.Set;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getCivilization;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class CivilizationCode {
    @CommandArg(name = "info")
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
        AISString info = person.getAISString(getInstance(), "civ-info")
                .replaceTag("{name}", civilization.getName())
                .replaceTag("{stage}", civilization.getStage().name().toLowerCase())
                ;

        String capitalString = "{}{pc} ◆ {mc}";
        AISString townString = new AISString("");
        Set<Town> towns = civilization.getTowns(); towns.remove(civilization.getCapital());
        if (!civilization.getLeaders().contains(rpg)) {
            capitalString += civilization.getCapital().getName();
            for (Town town : towns) townString.append("{}{pc} ◆ {mc}" + town.getName());
        } else { // Если лидер этой цивилизации
            capitalString += civilization.getCapital().toString();
            for (Town town : towns) townString.append("{}{pc} ◆ {mc}" + town.toString());
            info.append(
                    person.getAISString(getInstance(), "civ-info-advanced")
                            .replaceTag("{leaders}")
            );
        }
        person.send(info
                .replaceTag("{capital}", capitalString)
                .replaceTag("{towns}", townString)
        );
    }
}
