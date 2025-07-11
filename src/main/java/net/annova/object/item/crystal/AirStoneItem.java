package net.annova.object.item.crystal;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.object.item.base.ItemType.AIR_STONE;

public class AirStoneItem extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.AMETHYST_SHARD;
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
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @EventHandler
    public void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!AIR_STONE.use(player, event.getItem())) return;

        for (Entity entity : player.getNearbyEntities(8, 8, 8)) {
            if (entity instanceof LivingEntity ent) {
                ent.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 40, 0));
            }
            entity.setVelocity(entity.getLocation().toVector().subtract(player.getLocation().toVector())
                    .normalize().multiply(6)
            );
        }
        player.playSound(player, Sound.ITEM_MACE_SMASH_AIR, 1, 1);
    }
}
