package net.annova.item.crystal;

import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.util.BukkitObjects.chance;

public class DarkStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.DARK_PURPLE + "Кристалл тьмы";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{308};
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fЗаставляет всех сущностей рядом атаковать друг друга"
        );
    }

    @EventHandler
    private void drop(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if (player == null || player.getLocation().getBlock().getLightLevel() != 0) return;
        if (!chance(0.01)) return;
        event.getDrops().add(this.create());
    }
    
    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!equalItem(item)) return;

        this.removeItem(player, item);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1200, 1, false, false, true));

        for (Entity entity : player.getNearbyEntities(16, 12, 16)) {
            if (!(entity instanceof Mob mob)) continue;
            for (Entity target : entity.getNearbyEntities(8, 6, 8)) {
                if (!(target instanceof LivingEntity targ)) continue;
                mob.setTarget(targ);
                break;
            }
        }
    }
}
