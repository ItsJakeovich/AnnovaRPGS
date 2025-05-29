package net.annova.item.crystal;

import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.util.BukkitObjects.chance;

public class GravityStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.DARK_PURPLE + "Кристалл гравитации";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{322};
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fПри использовании защищает игрока от &eэффекта левитации"
        );
    }

    @EventHandler
    private void drop(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Shulker)) return;
        if (!chance(0.005)) return;
        event.getDrops().add(this.create());
    }
    @EventHandler
    private void mechanic(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!hasItem(player)) return;
        if (!event.getModifiedType().equals(PotionEffectType.LEVITATION)) return;
        if (!event.getAction().equals(EntityPotionEffectEvent.Action.ADDED)) return;
        player.getInventory().removeItem(this.create());
        player.playSound(player, Sound.BLOCK_PISTON_EXTEND, 1, 1);

        event.setCancelled(true);
    }
}
