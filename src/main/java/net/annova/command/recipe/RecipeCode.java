package net.annova.command.recipe;

import net.annova.command.register.DefaultArg;
import net.annova.event.custom.MenuClickItemCE;
import net.annova.object.person.Person;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

import static net.annova.main.AnnovaGlobal.getPerson;

public class RecipeCode implements Listener {
    @DefaultArg
    public void base(Player player, String[] args) {
        getPerson(player).openMenu(new RecipeInterface(), Map.of("page", 1));
    }

    @EventHandler
    public void menuClick(MenuClickItemCE event) {
        Person person = event.getPerson();
        if (person.getMenu() instanceof RecipeInterface menu) {
            person.openMenu(menu, Map.of("item_id", event.getKey()));
        }
    }
}
