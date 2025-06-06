package net.annova.object.item.crystal;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.object.item.base.Item.LIGHT_STONE;
import static net.annova.object.item.crystal.BalanceStoneItem.UNDEAD;

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
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3| &aНегативно &fвлияет на &6нежить &fв радиусе &615 &fблоков"
        );
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!LIGHT_STONE.use(player)) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 0));

        for (Entity check : player.getNearbyEntities(15, 10, 15)) {
            if (UNDEAD.contains(check.getType())) {
                LivingEntity entity = (LivingEntity) check;
                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 400, 0));
                entity.getWorld().strikeLightning(entity.getLocation());
            }
        }
    }
}
