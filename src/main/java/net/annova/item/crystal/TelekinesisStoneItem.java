package net.annova.item.crystal;

import net.annova.entity.traits.status.Status;
import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.chance;

public class TelekinesisStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.LIGHT_PURPLE + "Телекинезис";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{328};
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fСоздает слабое притягивающие магнитное поле"
        );
    }

    @EventHandler
    private void drop(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (!entity.getType().equals(EntityType.ENDERMAN)) return;
        if (!chance(0.005)) return;

        event.getDrops().add(this.create());
    }
    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!equalItem(item)) return;
        RPGPerson person = getRPGPerson(player);
        if (person.getStatus(Status.Type.TELEKINESIS) != null) return;
        person.addStatus(Status.Type.TELEKINESIS);
        player.playSound(player, Sound.BLOCK_PISTON_EXTEND, 1, 1);

        new BukkitRunnable() {
            public void run() {
                if (!hasItem(player)) {
                    person.removeStatus(Status.Type.TELEKINESIS);
                    cancel();
                }
                for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), 7, 7, 7)) {
                    if (entity.equals(player)) continue;
                    if (entity instanceof Player target) {
                        if (hasItem(target)) {
                            person.removeStatus(Status.Type.TELEKINESIS);
                            cancel();
                        }
                    }
                    Vector direction = player.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();

                    if (entity instanceof LivingEntity living)
                        living.setVelocity(direction.multiply(0.1));
                    else if (entity instanceof Item item)
                        item.setVelocity(direction.multiply(0.5));
                }
            }
        }.runTaskTimer(getInstance(), 0L, 5L);
    }
}
