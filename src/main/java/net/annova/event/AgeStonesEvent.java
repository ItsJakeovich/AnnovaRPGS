package net.annova.event;

import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import static net.annova.object.item.base.ItemType.CHRONO_STONE;
import static net.annova.object.item.base.ItemType.VITAL_STONE;
import static net.annova.main.AnnovaGlobal.getPerson;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class AgeStonesEvent implements Listener {
    @EventHandler
    public void drop(PlayerFishEvent event) {
        if (!event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) return;

        if (!chance(0.15)) return;
        ItemStack item;
        Player player = event.getPlayer();
        if (random.nextBoolean()) item = VITAL_STONE.create(player);
        else                      item = CHRONO_STONE.create(player);
        Location loc = event.getPlayer().getLocation();
        loc.getWorld().dropItemNaturally(loc, CraftItemStack.asBukkitCopy(item));
    }
}
