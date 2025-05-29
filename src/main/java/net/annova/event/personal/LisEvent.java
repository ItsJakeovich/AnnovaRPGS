package net.annova.event.personal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static net.annova.main.AnnovaRPG.getInstance;

public class LisEvent implements Listener {
    @EventHandler
    public void spawn(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.getName().equals("Lisyonochek")) return;
        if (!(event.getDamager() instanceof LivingEntity entity)) return;

        World world = player.getWorld();
        Location loc = player.getLocation();
        for (int i = 0; i < 3; i++) {
            Fox fox = (Fox) world.spawnEntity(loc, EntityType.FOX);
            fox.setCustomName(ChatColor.GOLD + player.getName() + ": лисичка");
            fox.setCustomNameVisible(true);
            fox.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!fox.isValid()) {
                        cancel();
                    }
                    fox.setTarget(entity);
                    fox.setVelocity(entity.getLocation().toVector().subtract(fox.getLocation().toVector()).normalize().multiply(2));
                }
            }.runTaskTimer(getInstance(), 0, 60L);
            Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), fox::remove, 300);
        }
    }
}
