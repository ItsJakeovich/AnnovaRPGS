package net.annova.object.item.base.attribute;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface RPGEquipment extends Listener {
    boolean effect(Player player);

    @EventHandler
    default void onEquip(InventoryClickEvent event) {

    }
}
