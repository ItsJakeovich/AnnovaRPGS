package net.annova.event;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class ItemHendRPGEvent implements Listener {
    @EventHandler
    public void held(PlayerItemHeldEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if (item == null) return;
        if (item.getType().name().contains("SPRUCE")) event.getPlayer().getInventory().setItem(event.getNewSlot(), new ItemStack(Material.WHEAT_SEEDS));
    }
}
