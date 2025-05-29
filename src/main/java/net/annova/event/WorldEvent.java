package net.annova.event;

import net.annova.item.base.RPGSet;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class WorldEvent implements Listener {
    @EventHandler
    private void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPlayedBefore()) return;
        getRPGPerson(player).updateHealth();
    }

    @EventHandler
    private void change(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World.Environment world = player.getWorld().getEnvironment();

        getRPGPerson(player).updateHealth();
    }

    @EventHandler
    private void damage(EntityDamageByEntityEvent event) {
        double addDamage = -2;
        switch (event.getEntity().getWorld().getEnvironment()) {
            case NETHER, THE_END -> addDamage = 2;
        }
        if (event.getDamager() instanceof Player player) {
            RPGSet set = getRPGPerson(player).getSet();
            if (set != null) {
                addDamage+=set.damage();
            }
        }
        event.setDamage(event.getDamage()+addDamage);
    }

    /* Позже доработать, должно корректно не уменьшать пищу в энде
    @EventHandler
    private void food(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getFoodLevel() < player.getFoodLevel()) return;

        double cancelChance = 0;
        switch (player.getWorld().getEnvironment()) {
            case THE_END -> cancelChance = 0.5;
        }
        if (chance(cancelChance)) event.setCancelled(true);
    }*/

    @EventHandler
    private void spawn(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Monster monster)) return;
        switch (entity.getType()) {
            case CREEPER:
            case WOLF:
            case FOX:
                return;
        }
        if (!chance(0.25)) return;

        PotionEffectType[] effects = new PotionEffectType[] {
                PotionEffectType.REGENERATION,
                PotionEffectType.RESISTANCE,
                PotionEffectType.STRENGTH,
                PotionEffectType.SPEED,
                PotionEffectType.JUMP_BOOST,
                PotionEffectType.GLOWING,
                PotionEffectType.OOZING
        };
        monster.addPotionEffect(new PotionEffect(effects[random.nextInt(effects.length)]
                , Integer.MAX_VALUE, random.nextInt(3)+1));
    }
}
