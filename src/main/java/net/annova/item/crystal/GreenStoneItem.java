package net.annova.item.crystal;

import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.chance;

public class GreenStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.GREEN + "Зелёный кристалл";
    }

    @Override
    public List<Float> modelData() {
        return List.of(301f);
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fДобавляет 4 доп. &c\uD83D\uDDA4 &fигроку"
        );
    }

    @EventHandler
    private void drop(EntityDeathEvent event) {
        if (!chance(0.005)) return;
        event.getDrops().add(this.create());
    }
    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!equalItem(item)) return;
        if (player.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) return;

        player.playSound(player, Sound.BLOCK_PISTON_EXTEND, 1, 1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 0, true, false, true));
        new BukkitRunnable() {
            public void run() {
                if (!hasItem(player)) {
                    player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
                    cancel();
                }
            }
        }.runTaskTimer(getInstance(), 0L, 200L);
    }
}
