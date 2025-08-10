package net.annova.command.civ.cmdBuild;

import net.annova.command.register.CommandArg;
import net.annova.command.register.DefaultArg;
import net.annova.environment.civilization.Town;
import net.annova.environment.civilization.build.BuildBase;
import net.annova.event.custom.MenuClickKeyCE;
import net.annova.main.AnnovaGlobal;
import net.annova.object.Confirm;
import net.annova.object.Person;
import net.annova.object.SimpleObject;
import net.annova.object.SimpleStructure;
import net.annova.object.person.RPGPerson;
import net.annova.util.MenuBase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.GlobalTasks.*;

public class BuildCode implements Listener {
    @DefaultArg
    public void onCommand(Player player, String[] args) {
        if (args.length != 0) return;
        RPGPerson rpg = getRPGPerson(player);
        Person person = rpg.getPerson();
        Town town = rpg.getSelectedTown();
        if (town == null) {
            person.send(getInstance(), "civ-info-cancel-town");
            return;
        }

        person.openMenu(new BuildInterface(), person, 1);
    }

    @CommandArg(name = "progress", aliases = {"прогресс", "п", "p", "зкщпкуыы", "ghjuhtcc"})
    public void progress(Player player) {
        RPGPerson rpg = getRPGPerson(player);
        Person person = rpg.getPerson();
        Town town = rpg.getSelectedTown();
        if (town == null) {
            person.send(getInstance(), "civ-info-cancel-town");
            return;
        }

        person.send(getInstance(), "build-progress", "{progress}", String.valueOf(town.getConstructions().getFirst().getCost()));
    }

    @EventHandler
    public void menuClick(MenuClickKeyCE event) {
        Person person = event.getPerson();
        MenuBase menu = person.getMenu();
        String key = event.getKey();
        System.out.println(key);
        if (key.startsWith("b-place-")) { // открывает меню со стилями
            person.openMenu(menu, person, BuildBase.valueOf(key.split("-")[2]));
            return;
        } else if (key.startsWith("preview:")) { // отсюда после /confirm начинается постройка.
            person.closeMenu(true);
            Confirm confirm = person.getConfirm();
            confirm.setConfirmType("build");
            SimpleStructure structure = AnnovaGlobal.getStructure(key.split(":")[1]);
            confirm.addData(new SimpleObject("structure", structure));
            cancelPreviewBuild(person);
            previewBuild(person, structure, getCenterByPlayer(structure, person.getServerPlayer()));
            person.send(getInstance(), "build-confirm");
        }

        int page;
        if (menu instanceof BuildInterface inf) page = inf.getPage();
        else return;
        if (key.equals("build-style-previous")) {
            person.openMenu(menu, person, page);
            return;
        }
        int delta;
        if (key.equals("build-previous")) delta = -1;
        else if (key.equals("build-next")) delta = 1;
        else return;

        person.openMenu(menu, person, delta+page);
    } // Поддтверждение в environment -> civilization -> event -> ConfirmEvent
}
