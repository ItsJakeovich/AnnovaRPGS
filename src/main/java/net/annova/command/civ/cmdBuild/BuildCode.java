package net.annova.command.civ.cmdBuild;

import net.annova.command.register.DefaultArg;
import net.annova.environment.civilization.Town;
import net.annova.event.custom.MenuClickKeyCE;
import net.annova.object.Person;
import net.annova.object.person.RPGPerson;
import net.annova.util.MenuBase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class BuildCode implements Listener {
    private static final MenuBase buildInterface = new BuildInterface();
    @DefaultArg
    public void onCommand(Player player, String[] args) {
        RPGPerson rpg = getRPGPerson(player);
        Person person = rpg.getPerson();
        Town town = rpg.getTown();
        if (town == null) {
            person.send(getInstance(), "civ-info-cancel-town");
            return;
        }

        person.openMenu(buildInterface, person, 1);
    }

    @EventHandler
    public void menuClick(MenuClickKeyCE event) {
        String key = event.getKey();
        int delta;
        if (key.equals("build-previous")) delta = -1;
        else if (key.equals("build-next")) delta = 1;
        else return;

        Person person = event.getPerson();
        int page;
        MenuBase menu = person.getMenu();
        if (menu instanceof BuildInterface inf) page = inf.getPage();
        else return;

        person.openMenu(menu, person, delta+page);
    }
}
