package net.annova.item.crystal;

import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;

import static net.annova.util.BukkitObjects.chance;

public class AirStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.AQUA + "Кристалл воздуха";
    }

    @Override
    public List<Float> modelData() {
        return List.of(319f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fНакладывает &eэффект левитации &fна близжайших &cсущностей"
        );
    }

    @EventHandler
    public void drop(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Shulker)) return;
        if (!chance(0.01)) return;
        event.getDrops().add(this.create());
    }

    @EventHandler
    public void mechanic(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (!equalItem(item)) return;

        Player player = event.getPlayer();
        Vector vector = player.getLocation().toVector();
        for (Entity entity : player.getNearbyEntities(10, 8, 10)) {
            if (entity instanceof LivingEntity ent) {
                ent.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 140, 0));
            }
            entity.setVelocity(entity.getLocation().toVector().subtract(vector).normalize().multiply(6));
        }
        this.removeItem(player, item);
        player.playSound(player, Sound.ITEM_MACE_SMASH_AIR, 1, 1);
    }
}
