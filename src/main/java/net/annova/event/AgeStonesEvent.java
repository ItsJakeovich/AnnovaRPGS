package net.annova.event;

import net.annova.item.crystal.ChronoStoneItem;
import net.annova.item.crystal.VitalStoneItem;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import static net.annova.util.BukkitObjects.chance;

public class AgeStonesEvent implements Listener {
    @EventHandler
    public void drop(PlayerFishEvent event) {
        if (!event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) return;

        if (!chance(0.15)) return;
        ItemStack item;
        if (chance(0.5)) item = new VitalStoneItem().create();
        else item = new ChronoStoneItem().create();
        Location loc = event.getPlayer().getLocation();
        loc.getWorld().dropItemNaturally(loc, item);
    }
}
