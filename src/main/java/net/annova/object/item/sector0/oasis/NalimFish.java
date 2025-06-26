package net.annova.object.item.sector0.oasis;

import net.annova.event.custom.MobDamageCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.annova.object.item.base.ItemType.NALIM_FISH;

public class NalimFish extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(10f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    private final Map<LivingEntity, Long> duration = new HashMap<>();
    @EventHandler
    public void consume(PlayerItemConsumeEvent event) {
        if (!NALIM_FISH.equals(event.getItem())) return;
        duration.put(event.getPlayer(), System.currentTimeMillis()+600_000);
    }
    @EventHandler
    public void onDamage(MobDamageCE event) {
        if (!event.getEvent().getCause().equals(EntityDamageEvent.DamageCause.DROWNING)) return;
        LivingEntity player = event.getEntity();
        Long until = duration.get(player);
        if (until != null) {
            if (System.currentTimeMillis() > until) {
                duration.remove(player);
                return;
            }

            event.decreaseDamage(event.getBaseDamage() * 0.9); // -90%
        }
    }
}
