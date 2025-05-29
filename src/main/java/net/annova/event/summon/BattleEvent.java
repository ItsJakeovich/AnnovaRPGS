package net.annova.event.summon;

import net.annova.event.custom.BattleCE;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BattleEvent implements Listener {
    @EventHandler
    public void summon(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof LivingEntity)) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;

        Bukkit.getPluginManager().callEvent(new BattleCE(event));
    }
}
