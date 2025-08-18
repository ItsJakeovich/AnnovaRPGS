package net.annova.command.civ.cmdBuild;

import net.annova.command.register.CommandArg;
import net.annova.command.register.DefaultArg;
import net.annova.environment.civilization.Town;
import net.annova.environment.civilization.build.BuildBase;
import net.annova.event.custom.MenuClickKeyCE;
import net.annova.main.AnnovaGlobal;
import net.annova.object.person.Confirm;
import net.annova.object.person.Person;
import net.annova.object.SimpleObject;
import net.annova.object.SimpleStructure;
import net.annova.object.person.RPGPerson;
import net.annova.util.menu.MenuBase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

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

        Map<String, Object> data = new HashMap<>();
        data.put("page", 1);
        person.openMenu(new BuildInterface(), person, data);
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
        if (key.startsWith("b-place-")) { // открывает меню со стилями
            Map<String, Object> data = new HashMap<>();
            data.put("build", BuildBase.valueOf(key.split("-")[2]));
            person.openMenu(menu, person, data);
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
    } // Поддтверждение в environment -> civilization -> event -> ConfirmEvent
}
