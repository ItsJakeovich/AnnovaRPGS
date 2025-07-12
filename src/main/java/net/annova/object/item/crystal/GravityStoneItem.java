package net.annova.object.item.crystal;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.object.item.base.ItemType.GRAVITY_STONE;

public class GravityStoneItem extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(322f);
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @EventHandler
    private void mechanic(EntityPotionEffectEvent event) {
        if (event.getEntity() instanceof LivingEntity entity) {
            if (!event.getModifiedType().equals(PotionEffectType.LEVITATION)) return;
            if (!event.getAction().equals(EntityPotionEffectEvent.Action.ADDED)) return;
            if (!GRAVITY_STONE.removeItem(entity)) return;
            entity.getWorld().playSound(entity, Sound.BLOCK_PISTON_EXTEND, 1, 1);

            event.setCancelled(true);
        }
    }
}
