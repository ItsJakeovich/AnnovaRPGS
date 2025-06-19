package net.annova.object.person.equipment.event;

import net.annova.event.custom.MouseClickCE;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EquipEvent implements Listener {
    @EventHandler
    public void equip(MouseClickCE event) {
        if (event.getClickType().equals(MouseClickCE.ClickType.LEFT_CLICK)) return;
        event.getPerson().getEquipment().addEquipment(event.getPlayer(), event.getEvent().getItem());
    }
}
