package net.annova.object.person.equipment.event;

import net.annova.object.person.RPGPerson;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class InventoryEvent implements Listener {
    @EventHandler
    public void inventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        RPGPerson person = getRPGPerson(player);
        int slot = event.getSlot();
        // Open inventory action
        if (event.getInventory().getType().equals(InventoryType.CRAFTING)) {
            if (slot == -999 && event.getCursor().getType().isAir()) {
                person.getEquipment().open(person.getPerson());
                event.setCancelled(true);
            }
        }
        if (event.getView().getTitle().equals("Equipment")) {
            event.setCancelled(true);
            // Close inventory action
            if (slot == -999) {
                player.closeInventory();
                return;
            }
            ItemStack current = event.getCurrentItem();
            if (current == null) return;
            if (current.getItemMeta().getDisplayName().equals(" ")) return;
            InventoryType type = event.getClickedInventory().getType();
            switch (type) {
                case CHEST -> person.getEquipment().removeEquipment(current, slot); // move from equipment
                case PLAYER -> person.getEquipment().addEquipment(current, slot); // move to equipment
            }
        }
    }
}
