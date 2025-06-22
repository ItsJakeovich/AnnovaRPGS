package net.annova.event.summon;

import net.annova.event.custom.BattleCE;
import net.annova.event.custom.LivingEntityDamageByEntityCE;
import net.annova.event.custom.MobDamageCE;
import net.annova.event.custom.PlayerDamageCE;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.random;

public class EntityEvent implements Listener {
    @EventHandler
    public void damageCESummoner(org.bukkit.event.entity.EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerDamageCE ev = new PlayerDamageCE(player, event);
            this.baseFunction(event, ev);
        } else if (event.getEntity() instanceof Mob entity) {
            MobDamageCE ev = new MobDamageCE(entity, event);
            this.baseFunction(event, ev);
        }
    }
    @EventHandler
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
        if (ev.isCancelled()) {
            event.setCancelled(true);
            return;
        } else if (ev.getEntity() instanceof Player player) {
            event.setCancelled(true);
            if (player.getNoDamageTicks() != 0) return;

            getRPGPerson(player).damageWithCooldown(player, (float) ev.getDamage());
        } else {
            event.setDamage(ev.getDamage());
        }
        this.spawnIndicator(event.getEntity().getLocation().add(0, 1.5, 0), ev.getDamage());
    }

    public static List<ArmorStand> indicators = new ArrayList<>();
    private void spawnIndicator(Location entityLocation, double damage) {
        if (damage < 1) return;
        Location loc = entityLocation.clone().add(0, 1.5, 0);
        loc.add(random.nextFloat(2) - 1, random.nextFloat(2) - 1, random.nextFloat(2) - 1);
        ArmorStand armorStand = loc.getWorld().spawn(loc, ArmorStand.class, stand -> {
            stand.setMarker(true);
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setSmall(true);
            stand.setCustomNameVisible(true);
            stand.setCustomName(ChatColor.BOLD + "" + ChatColor.RED + (double) ((int) (damage * 100)) / 100);
        });
        indicators.add(armorStand);
        Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () -> {
            armorStand.remove();
            indicators.add(armorStand);
        }, 25L);
    }
}
