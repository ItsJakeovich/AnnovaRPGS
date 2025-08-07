package net.annova.object.item.base.event;

import net.annova.event.custom.RightClickCE;
import net.annova.object.item.base.RPGItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.annova.object.item.base.ItemType.getRPGItem;

public class InteractEvent implements Listener {
    @EventHandler
    public void interact(RightClickCE event) {
        RPGItem item = getRPGItem(event.getItemInHand());
        if (item == null) return;
        if (item.unusable()) event.setCancelled(true);
    }
}
