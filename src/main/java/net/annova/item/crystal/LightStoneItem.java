package net.annova.item.crystal;

import net.annova.item.base.RPGItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.util.BukkitObjects.chance;

public class LightStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.YELLOW + "Кристалл света";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{318};
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
    private void drop(EntityDeathEvent event) {
        if (event.getEntity().getLocation().getBlock().getLightLevel() < 12) return;
        if (!chance(0.05)) return;
        event.getDrops().add(this.create());
    }
    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!equalItem(item)) return;

        this.removeItem(player, item);
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 0));

        for (Entity entity : player.getNearbyEntities(15, 10, 15)) {
            if (!(entity instanceof LivingEntity)) continue;
            switch (entity.getType()) {
                case SKELETON:
                case ZOMBIE:
                case HUSK:
                case STRAY:
                case DROWNED:
                case WITHER_SKELETON:
                case ZOMBIFIED_PIGLIN:
                case ZOMBIE_VILLAGER:
                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 400, 0));
                    ((LivingEntity) entity).damage(25);
                    Block block = entity.getLocation().getBlock();
                    Material mat = block.getType();
                    block.setType(Material.LIGHT);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () -> block.setType(mat), 20L);
                    break;
            }
        }
    }
}
