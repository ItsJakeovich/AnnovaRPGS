package net.annova.item.oasis;

import net.annova.item.base.RPGItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.annova.item.base.Item.NALIM_FISH;

public class NalimFish extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of();
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of("&3= &fОбитатель самых тёмных и глубоких вод&3, &fналим впитал в себя терпение и силу подводного мира&3. &fЕго плоть наделяет едока способностью дышать почти как рыба &3— &fне полностью&3, &fно достаточно&3, &fчтобы вода не причиняла боли&3.");
    }

    private final Map<Player, Long> duration = new HashMap<>();
    @EventHandler
    public void consume(PlayerItemConsumeEvent event) {
        if (!NALIM_FISH.equals(event.getItem())) return;
        duration.put(event.getPlayer(), System.currentTimeMillis()+600_000);
    }
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)) return;
        if (event.getEntity() instanceof Player player) {
            Long until = duration.get(player);
            if (until != null) {
                if (System.currentTimeMillis() > until) {
                    duration.remove(player);
                    return;
                }
                event.setDamage(event.getDamage() * 0.1);
            }
        }
    }
}
