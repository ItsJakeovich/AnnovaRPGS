package net.annova.command.recipe;

import net.annova.command.register.DefaultArg;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static net.annova.main.AnnovaGlobal.getPerson;

public class RecipeCode {
    @DefaultArg
    public void base(Player player, String[] args) {
        Map<String, Object> data = new HashMap<>();
        data.put("page", 1);
        getPerson(player).openMenu(new RecipeInterface(), data);
    }
}
