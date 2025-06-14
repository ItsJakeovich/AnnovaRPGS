package net.annova.command.cmdRPG;

import net.annova.command.register.CommandArg;
import net.annova.object.entity.core.RPGEntityType;
import net.annova.object.item.base.Item;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

import static net.annova.environment.world.sector0.SuruWahare.sectorZones;
import static net.annova.environment.world.sector0.SuruWahare.world0;

public class RPGCode {
    private static final List<Inventory> itemsInterface = new ArrayList<>();
    static {
        int i = 0;
        Inventory inventory = null;
        for (Item item : Item.values()) {
            if (i % 54 == 0) {
                inventory = Bukkit.createInventory(null, 54, "Items - page: " + 1 + i/54);
                itemsInterface.add(inventory);
            }
            inventory.addItem(item.create(1));
            i++;
        }
    }
    @CommandArg(name = "items")
    public void items(Player player, String[] args) {
        String wrong = "Pages: " + Math.ceil((double)(Item.values().length)/54) + " (/rpg items {page})";
        if (args.length == 1) {
            player.sendMessage(wrong);
            return;
        }
        int page = Integer.parseInt(args[1]);
        if (page > itemsInterface.size()) {
            player.sendMessage(wrong);
            return;
        }
        Inventory template = itemsInterface.get(page-1);
        Inventory toOpen = Bukkit.createInventory(null, 54, "Items - page: " +page);
        toOpen.setContents(template.getContents());
        player.openInventory(toOpen);
    }

    @CommandArg(name = "show-borders")
    public void showBorders(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage("Usage: /rpg show-borders {1-6}");
            return;
        }
        int location = Integer.parseInt(args[1]);
        sectorZones.get(location).showTerritory(world0);
    }

    @CommandArg(name = "summon")
    public void summon(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage("Usage: /rpg summon {entity-name}");
            return;
        }
        RPGEntityType type = RPGEntityType.valueOf(args[1]);
        type.getEntity().create(player.getLocation());
    }
}
