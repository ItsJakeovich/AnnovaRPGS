package net.annova.item.equipment.perk;

import net.annova.event.custom.BattleCE;
import net.annova.item.base.RPGTool;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static net.annova.environment.dungeon.Loot.random;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.item.base.Tool.getTool;

public class Bleeding implements Listener {
    @EventHandler
    private void activity(BattleCE event) {
        EntityDamageByEntityEvent ev = event.getEvent();
        if (!(ev.getDamager() instanceof Player player)) return;
        RPGTool tool = getTool(player.getInventory().getItemInMainHand());
        if (tool == null) return;
        if (!tool.perks().contains(Perk.BLEEDING)) return;

        new BukkitRunnable() {
            final LivingEntity target = (LivingEntity) ev.getEntity();
            final int max = random.nextInt(4)+3;
            int i = 0;

            @Override
            public void run() {
                if (i > max || !target.isValid()) this.cancel();
                target.damage(1);
                i++;
            }
        }.runTaskTimer(getInstance(), 0L, 10L);
    }
}
