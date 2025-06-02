package net.annova.object.item.crystal;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
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

import static net.annova.object.item.base.Item.AIR_STONE;
import static net.annova.main.AnnovaGlobal.getPerson;

public class AirStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
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
        return RPGRarity.RARE;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fНакладывает &eэффект левитации &fна близжайших &cсущностей"
        );
    }

    @EventHandler
    public void drop(EntityDeathEvent event) {
        if (event.getEntity() instanceof Shulker shulker) {
            event.getDrops().add(AIR_STONE.create(getPerson(shulker.getKiller()), 0.01));
        }
    }

    @EventHandler
    public void mechanic(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (!AIR_STONE.equals(item)) return;

        Player player = event.getPlayer();
        Vector vector = player.getLocation().toVector();
        for (Entity entity : player.getNearbyEntities(10, 8, 10)) {
            if (entity instanceof LivingEntity ent) {
                ent.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 140, 0));
            }
            entity.setVelocity(entity.getLocation().toVector().subtract(vector).normalize().multiply(6));
        }
        subtractItem(player, item);
        player.playSound(player, Sound.ITEM_MACE_SMASH_AIR, 1, 1);
    }
}
