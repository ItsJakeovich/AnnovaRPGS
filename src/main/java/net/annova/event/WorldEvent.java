package net.annova.event;

import net.annova.environment.world.advancement.RPGAdvancement;
import net.annova.object.person.RPGPerson;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static net.annova.config.AnnovaConfig.sendInfo;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.util.BukkitObjects.random;

public class WorldEvent implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        RPGPerson person = getRPGPerson(player);
        person.updateHealth();

        for (RPGAdvancement rpg : RPGAdvancement.values()) {
            Advancement advancement = rpg.resolve();
            if (advancement == null) {
                sendInfo(STR."&e|= Missing advancement: \{rpg.getKey().getKey()}");
                continue;
            }
            if (player.getAdvancementProgress(advancement).isDone()) {
                person.addAdvancement(rpg);
            }
        }
    }

    @EventHandler
    public void change(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World.Environment world = player.getWorld().getEnvironment();

        getRPGPerson(player).updateHealth();
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
