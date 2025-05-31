package net.annova.item.crystal;

import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

import static net.annova.item.base.Item.AQUA_STONE;
import static net.annova.util.BukkitObjects.chance;

public class AquaStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(304f, 204f, 312f, 1262f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fПри использовании накладывает эффект &bГрация дельфина",
                "&3= &fПри использовании накладывает эффект &bПодводное дыхание"
        );
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (AQUA_STONE.use(player, item)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 1200, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 1200, 0));
        }
    }
}
