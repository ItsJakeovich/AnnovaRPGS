package net.annova.event;

import net.annova.environment.civilization.Civilization;
import net.annova.environment.civilization.Town;
import net.annova.event.custom.InitScoreboardCE;
import net.annova.lang.AISString;
import net.annova.object.person.Person;
import net.annova.object.person.Scoreboard;
import net.annova.object.person.ScoreboardLine;
import net.annova.object.person.RPGPerson;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class ScoreboardRPGEvent implements Listener {
    @EventHandler
    public void init(InitScoreboardCE event) {
        Person person = event.getPerson();
        RPGPerson rpg = getRPGPerson(person);
        Scoreboard scoreboard = event.getScoreboard();
        scoreboard.addLore(2, new ScoreboardLine(person, "culture", person.getAISString(getInstance(), "board-culture"), getInfo(rpg, "culture")));
        if (rpg.getTown() != null) {
            scoreboard.addLore(6, new ScoreboardLine(person, "town", person.getAISString(getInstance(), "board-town"), getInfo(rpg, "town")));
            scoreboard.addLore(6, new ScoreboardLine(person, "civ", person.getAISString(getInstance(), "board-civ"), getInfo(rpg, "civ")));
        }
    }

    public static AISString getInfo(RPGPerson person, String key) {
        switch (key) {
            case "culture" -> {
                Town area = person.getCurrentArea();
                if (area == null) return person.getPerson().getAISString(getInstance(), "wasteland");
                return new AISString((area.getMembers().contains(person) ? "{tc}" : "{fc}") + area.getName());
            }
            case "town" -> {
                Town town = person.getTown();
                String color = "{mc}";
                if (town.getLeaders().contains(person)) color = "{ac}";
                return new AISString(color + person.getTown());
            }
            case "civ" -> {
                Civilization civilization = person.getTown().getCivilization();
                String color = "{mc}";
                if (civilization != null && civilization.getLeaders().contains(person)) color = "{ac}";
                return new AISString(color + civilization);
            }
        }
        return null;
    }
}
