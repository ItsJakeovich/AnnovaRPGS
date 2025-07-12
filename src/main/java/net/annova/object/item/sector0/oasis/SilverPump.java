package net.annova.object.item.sector0.oasis;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.List;
import static net.annova.object.item.base.ItemType.SILVER_PUMP;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.person.RPGPerson.teleportHome;

public class SilverPump extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(19f);
    }

    @Override
    public int stackSize() {
        return 16;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
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
