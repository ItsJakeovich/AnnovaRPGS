package net.annova.event.summon;

import net.annova.event.custom.MouseClickCE;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class ClickRPGEvent implements Listener {
    @EventHandler
    public void click(PlayerInteractEvent event) {
        EquipmentSlot slot = event.getHand();
        if (slot == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) return;

        MouseClickCE.ClickType type = null;
        Action action = event.getAction();
        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) type = MouseClickCE.ClickType.RIGHT_CLICK;
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) type = MouseClickCE.ClickType.LEFT_CLICK;

        if (type == null) return;
        MouseClickCE ev = new MouseClickCE(event.getPlayer(), event, type);
        Bukkit.getPluginManager().callEvent(ev);
    }
}
