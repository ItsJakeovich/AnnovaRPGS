package net.annova.event.summon;

import net.annova.event.custom.BattleCE;
import net.annova.event.custom.EntityDamageByEntityCE;
import net.annova.event.custom.EntityDamageCE;
import net.annova.event.custom.PlayerDamageCE;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class EntityEvent implements Listener {
    @EventHandler
    public void damageCESummoner(org.bukkit.event.entity.EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity entity) {
            EntityDamageCE ev = new EntityDamageCE(entity, event);
            this.baseFunction(event, ev);
        }
        if (event.getEntity() instanceof Player player) {
            PlayerDamageCE ev = new PlayerDamageCE(player, event);
            Bukkit.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                event.setCancelled(true);
                return;
            }
            event.setDamage(0.1);
            getRPGPerson(player).damage((int) ev.getDamage());
        }
    }
    @EventHandler
    public void damageByEntitySummoner(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity entity) {
            EntityDamageByEntityCE ev = new EntityDamageByEntityCE(entity, event);
            this.baseFunction(event, ev);
        }
    }
    @EventHandler
    public void battleSummoner(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity entity && event.getDamager() instanceof LivingEntity damager) {
            BattleCE ev = new BattleCE(entity, damager, event);
            this.baseFunction(event, ev);
        }
    }

    private void baseFunction(org.bukkit.event.entity.EntityDamageEvent event, EntityDamageCE ev) {
        Bukkit.getPluginManager().callEvent(ev);
        if (ev.isCancelled()) {
            event.setCancelled(true);
        } else if (ev.getEntity() instanceof Player player) {
            event.setDamage(0.1);
            getRPGPerson(player).damage((int) ev.getDamage());
        } else event.setDamage(ev.getDamage());
    }
}
