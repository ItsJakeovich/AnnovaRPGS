package net.annova.object.item.crystal;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.object.item.base.Item.GRAVITY_STONE;

public class GravityStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(322f);
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3| &aЗащищает &fигрока от &eэффекта левитации"
        );
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
