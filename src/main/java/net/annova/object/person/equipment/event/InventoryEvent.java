package net.annova.object.person.equipment.event;

import net.annova.object.person.RPGPerson;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.getNewIcon;

public class InventoryEvent implements Listener {
    @EventHandler
    public void inventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        RPGPerson person = getRPGPerson(player);
        person.getPerson().send(event.getInventory().getType() + ", slot: " + event.getSlot());
        if (event.getInventory().getType().equals(InventoryType.CRAFTING)) {
            if (event.getSlot() == 9) {
                event.setCancelled(true);
                person.getEquipment().open(player);
            }
        }
        if (event.getView().getTitle().equals(" ")) {
            event.setCancelled(true);
            ItemStack current = event.getCurrentItem();
            if (current == null) return;
            if (current.getItemMeta().getDisplayName().equals(" ")) return;
        }
    }

    public static final ItemStack equipmentIcon = getNewIcon(0f);
    @EventHandler
    public void open(InventoryOpenEvent event) {
        if (event.getInventory().getType().equals(InventoryType.CRAFTING)) {
            event.getInventory().setItem(9, equipmentIcon);
        }
    }
}
