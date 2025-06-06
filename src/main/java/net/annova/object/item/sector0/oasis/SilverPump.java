package net.annova.object.item.sector0.oasis;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.List;
import static net.annova.object.item.base.Item.SILVER_PUMP;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.person.RPGPerson.teleportHome;

public class SilverPump extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(19f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public List<String> lore() {
        return List.of("&3= &fРедкая рыба&3, &fсвязанная с личными воспоминаниями&3. &fПри поедании она вызывает тёплое чувство уюта и желания вернуться \"домой\"&3, &fгде бы это ни было");
    }

    @EventHandler
    public void consume(PlayerItemConsumeEvent event) {
        if (!SILVER_PUMP.equals(event.getItem())) return;

        Player player = event.getPlayer();
        final double health = player.getHealth();
        Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), () -> {
            if (player.getHealth() < health) return;

            teleportHome(player);
        }, 168);
    }
}
