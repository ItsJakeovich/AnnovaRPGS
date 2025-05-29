package net.annova.command.cmdInventory;

import net.annova.command.register.DefaultArg;
import net.annova.main.AnnovaAPI;
import net.annova.object.Person;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.main.AnnovaRPG.getInstance;

public class InventoryCode {
    @DefaultArg(arguments = "{players}")
    public void inventory(Player player, String[] args) {
        Person person = getPerson(player);
        boolean ender = false;
        if (args.length == 0) {
            person.send(getInstance(), "invsee-args");
            return;
        }
        if (args.length > 1) {
            if (args[1].equals("ec") || args[1].equals("ус")) ender = true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            person.send(AnnovaAPI.getInstance(), "no-target");
            return;
        }

        player.openInventory(ender ? target.getEnderChest() : target.getInventory());
    }
}
