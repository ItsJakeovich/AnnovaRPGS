package net.annova.object.item.crystal;

import net.annova.event.custom.MouseClickCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.object.item.base.ItemType.LIGHT_STONE;
import static net.annova.object.item.crystal.BalanceStoneItem.UNDEAD;

public class LightStoneItem extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.AMETHYST_SHARD;
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
        return RPGRarity.UNCOMMON;
    }

    @EventHandler
    private void mechanic(MouseClickCE event) {
        if (event.getClickType().equals(MouseClickCE.ClickType.LEFT_CLICK)) return;
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
