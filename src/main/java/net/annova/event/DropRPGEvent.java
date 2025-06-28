package net.annova.event;

import net.annova.event.custom.DropItemFoundBlockCE;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.entity.mobs.sector0.oasis.AnglerFish.FISH_ITEMS;

public class DropRPGEvent implements Listener {
    @EventHandler
    public void drop(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        final Player player = event.getPlayer();

        new BukkitRunnable() {
            private Location oldLocation;

            public void run() {
                Location location = item.getLocation();
                if (!item.isValid() || location.equals(oldLocation)) {
                    this.cancel();
                }
                Block block = location.add(item.getVelocity().normalize()).getBlock();
                if (!block.isEmpty()) {
                    ItemStack stack = item.getItemStack();
                    DropItemFoundBlockCE e = new DropItemFoundBlockCE(player, stack, block);
                    player.sendMessage(block.getType().name());
                    Bukkit.getPluginManager().callEvent(e);

                    // Если рыба - уплывёт
                    if (block.getType().equals(Material.WATER)) {
                        Material material = stack.getType();
                        for (Material fish : FISH_ITEMS) {
                            if (fish.equals(material)) {
                                World world = location.getWorld();
                                world.spawnParticle(Particle.BUBBLE, location, 20, 0.2, 0.05, 0.2);
                                world.playSound(location, Sound.ENTITY_FISHING_BOBBER_SPLASH, 1, 1);
                                item.remove();
                                break;
                            }
                        }
                    }
                    this.cancel();
                    return;
                }
                this.oldLocation = location;
            }
        }.runTaskTimer(getInstance(), 0L, 1L);
    }
}
