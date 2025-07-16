package net.annova.event.summon;

import net.annova.event.custom.BattleCE;
import net.annova.event.custom.LivingEntityDamageByEntityCE;
import net.annova.event.custom.MobDamageCE;
import net.annova.event.custom.PlayerDamageCE;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.ArrayList;
import java.util.List;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.random;

public class EntityEvent implements Listener {
    @EventHandler(priority = EventPriority.LOWEST) // Отключение восстановления здоровья
    public void regain(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void damageCESummoner(org.bukkit.event.entity.EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerDamageCE ev = new PlayerDamageCE(player, event);
            this.baseFunction(event, ev);
        } else if (event.getEntity() instanceof Mob entity) {
            MobDamageCE ev = new MobDamageCE(entity, event);
            this.baseFunction(event, ev);
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void damageByEntitySummoner(EntityDamageByEntityEvent event) {
        Entity eventEntity = event.getEntity();
        Entity eventDamager = event.getDamager();

        if (eventEntity instanceof LivingEntity entity && eventDamager instanceof LivingEntity damager) {
            BattleCE ev = new BattleCE(entity, damager, event);
            Bukkit.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                event.setCancelled(true);
                return;
            }
            event.setDamage(ev.getDamage());
        } else if (eventEntity instanceof LivingEntity entity) {
            LivingEntityDamageByEntityCE ev = new LivingEntityDamageByEntityCE(entity, event);
            Bukkit.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                event.setCancelled(true);
                return;
            }
            event.setDamage(ev.getDamage());
        }
    }

    private void baseFunction(org.bukkit.event.entity.EntityDamageEvent event, MobDamageCE ev) {
        Bukkit.getPluginManager().callEvent(ev);
        event.setDamage(ev.getDamage());
        if (ev.isCancelled()) {
            event.setCancelled(true);
            return;
        }
        event.setDamage(ev.getDamage()); // Это и для моба, и для игрока.
        double finalDamage = event.getFinalDamage();

        if (ev.getEntity() instanceof Player player) { // для игрока
            event.setCancelled(true);
            if (player.getNoDamageTicks() != 0) return;

            event.setDamage(ev.getDamage());
            Entity bukkit = event.getDamageSource().getCausingEntity();
            getRPGPerson(player).damageWithCooldown(bukkit == null ? null : ((CraftEntity) bukkit).getHandle(),
                    (float) finalDamage
            );
        }
        Player player;
        this.spawnIndicator(((CraftLivingEntity) event.getEntity()).getEyeLocation().add(0, 1.5, 0), finalDamage);
    }

    public static List<ArmorStand> indicators = new ArrayList<>();

    private void spawnIndicator(Location location, double damage) {
        if (damage < 1) return;
        location.add(random.nextFloat(2) - 1, random.nextFloat(2) - 1, random.nextFloat(2) - 1);
        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class, stand -> {
            stand.setMarker(true);
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setSmall(true);
            stand.setCustomNameVisible(true);
            stand.setCustomName(ChatColor.BOLD + "" + ChatColor.RED + (double) Math.round(damage * 100) / 100);
        });
        indicators.add(armorStand);
        Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () -> {
            armorStand.remove();
            indicators.add(armorStand);
        }, 25L);
    }
}
