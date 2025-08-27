package net.annova.object.item.base.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static net.annova.object.item.base.ItemType.ABSORBS_NIGHT_RING;

public class EffectRPGEvent implements Listener {
    @EventHandler
    public void effect(EntityPotionEffectEvent event) {
        PotionEffect effect = event.getNewEffect();
        if (effect != null && effect.getType() == PotionEffectType.DARKNESS && event.getEntity() instanceof Player player) {
            if (ABSORBS_NIGHT_RING.hasEquipment(player)) event.setCancelled(true);
        }
    }
}
