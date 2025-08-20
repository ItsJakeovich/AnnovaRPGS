package net.annova.object.person.equipment.event;

import net.annova.event.custom.MenuClickItemCE;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.equipment.EquipmentMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.minecraft.core.component.DataComponents.CUSTOM_NAME;

public class InventoryEvent implements Listener {
    @EventHandler
    public void inventory(InventoryClickEvent event) {
        // Open inventory action
        if (event.getSlot() == -999 && event.getInventory().getType().equals(InventoryType.CRAFTING) && event.getCursor().getType().isAir()) {
            RPGPerson person = getRPGPerson(event.getWhoClicked().getUniqueId());
            person.getEquipment().open(person.getPerson());
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void equipmentMenu(MenuClickItemCE event) {
        if (event.getPerson().getMenu() instanceof EquipmentMenu) {
            ItemStack item = event.getItem();
            if (item.get(CUSTOM_NAME).equals(Component.empty())) return;
            RPGPerson person = getRPGPerson(event.getPerson());
            if (event.getContainer() instanceof InventoryMenu) {
                person.getEquipment().addEquipment(item, event.getSlot());
            } else {
                person.getEquipment().removeEquipment(item, event.getSlot());
            }
        }
    }
}
