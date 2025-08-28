package net.annova.event;

import net.annova.event.custom.DropItemFoundBlockCE;
import net.annova.object.person.advancement.RPGAdvancement;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.entity.mobs.sector0.oasis.AnglerFish.FISH_ITEMS;

public class DropRPGEvent implements Listener {
    @EventHandler
    public void drop(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        final Player player = event.getPlayer();

        new BukkitRunnable() {
            public void run() {
                Location location = item.getLocation().clone();
                Vector velocity = item.getVelocity().clone();
                double length = velocity.lengthSquared();
                if (!item.isValid() || length < 0.0001) {
                    this.cancel();
                    return;
                }
                velocity.normalize();
                location.add(velocity);
                if (location.getX() > 10000 || location.getX() < -10000 || location.getZ() > 10000 || location.getZ() < -10000) {
                    this.cancel();
                    return;
                }
                Block block = location.getBlock();
                if (block.isEmpty()) return;

                DropItemFoundBlockCE dropItemCE = new DropItemFoundBlockCE(player, ((CraftItem) item).getHandle().getItem(), block);
                PluginManager pm = Bukkit.getPluginManager();
                pm.callEvent(dropItemCE);

                // Если рыба уплывёт
                ItemStack stack = item.getItemStack();
                if (block.getType().equals(Material.WATER)) {
                    Material material = stack.getType();
                    for (Material fish : FISH_ITEMS) {
                        if (fish.equals(material)) {
                            FishSwamAwayCE swamAwayCE = new FishSwamAwayCE(player, stack);
                            pm.callEvent(swamAwayCE);

                            World world = location.getWorld();
                            world.spawnParticle(Particle.BUBBLE, location, 20, 0.2, 0.05, 0.2);
                            world.playSound(location, Sound.ENTITY_FISHING_BOBBER_SPLASH, 1, 1);
                            item.remove();

                            getRPGPerson(player).completeAdvancement(RPGAdvancement.SWIM_LITTLE_FISH);
                            break;
                        }
                    }
                }
                this.cancel();
            }
        }.runTaskTimer(getInstance(), 0L, 2L);
    }
}
