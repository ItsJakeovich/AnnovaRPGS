package net.annova.item.crystal;

import net.annova.item.base.RPGItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.item.base.Item.LIGHT_STONE;
import static net.annova.main.AnnovaRPG.getInstance;

public class LightStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData()  {
        return List.of(318f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fНегативно влияет на &3нежить &fв радиусе 15 блоков"
        );
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!LIGHT_STONE.use(player, item)) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 0));

        for (Entity entity : player.getNearbyEntities(15, 10, 15)) {
            if (!(entity instanceof LivingEntity living)) continue;
            switch (entity.getType()) {
                case SKELETON:
                case ZOMBIE:
                case HUSK:
                case STRAY:
                case DROWNED:
                case WITHER_SKELETON:
                case ZOMBIFIED_PIGLIN:
                case ZOMBIE_VILLAGER:
                    living.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 400, 0));
                    living.damage(25);

                    Location location = entity.getLocation();
                    location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 1);
                    Block block = entity.getLocation().getBlock();
                    Material mat = block.getType();
                    block.setType(Material.LIGHT);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () -> block.setType(mat), 20L);
                    break;
            }
        }
    }
}
