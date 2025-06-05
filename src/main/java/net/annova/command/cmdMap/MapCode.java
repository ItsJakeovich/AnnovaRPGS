package net.annova.command.cmdMap;

import net.annova.command.register.DefaultArg;
import net.annova.object.Person;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;

public class MapCode {
    @DefaultArg(arguments = {"here"})
    public void map(Player player, String[] args) {
        Person person = getPerson(player);

        String coords = "";
        if (args.length != 0) {
            Location loc = player.getLocation();
            if (args[0].startsWith("h")) coords = STR."?worldname=\{loc.getWorld().getName()}&mapname=flat&zoom=2&x=\{loc.getBlockX()}&y=\{loc.getBlockY()}&z=\{loc.getBlockZ()}";
        }
        person.sendURL(getInstance(), "map-url", STR."http://185.230.64.61:8123/\{coords}");
    }
}
