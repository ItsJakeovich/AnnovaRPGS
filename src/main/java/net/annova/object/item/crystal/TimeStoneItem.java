package net.annova.object.item.crystal;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.TIME_STONE;
import static net.annova.object.person.status.StatusType.STOPPED;

public class TimeStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(330f);
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &f Останавливает время в радиусе на 5 секунд"
        );
    }

    private static final Set<Entity> stopped = new HashSet<>();
    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        if (!TIME_STONE.use(player, item)) return;

        long ticks = 100;
        for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
            stopped.add(entity);
            entity.setGravity(false);
            if (entity instanceof LivingEntity living) {
                living.setAI(false);
                if (living instanceof Player target) getRPGPerson(target).getStatusManager().addStatus(STOPPED);
            }

            new BukkitRunnable() {
                private final Location loc = entity.getLocation();
                @Override
                public void run() {
                    if (!stopped.contains(entity)) this.cancel();
                    entity.teleport(loc);
                }
            }.runTaskTimer(getInstance(), 0L, 1L);

            Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () -> {
                entity.setGravity(true);
                if (entity instanceof LivingEntity living) {
                    living.setAI(true);
                    if (living instanceof Player target) {
                        RPGPerson pers = getRPGPerson(target);
                        pers.getStatusManager().removeStatus(STOPPED);
                    }
                }
                stopped.remove(entity);
            }, ticks);
        }
    }
}
