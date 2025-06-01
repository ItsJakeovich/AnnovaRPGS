package net.annova.item.crystal;

import net.annova.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.annova.item.base.Item.RETURN_STONE;
import static net.annova.main.GlobalRPG.getDungeonWorld;
import static net.annova.object.person.RPGPerson.teleportHome;
import static net.annova.util.BukkitObjects.addItems;
import static net.annova.util.BukkitObjects.chance;

public class ReturnStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(326f);
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
                "&3= &fПри использовании возвращает вас на точку вашего спавна"
        );
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        if (event.getAction().name().charAt(0) != 'R') return;
        Player player = event.getPlayer();
        if (player.getWorld().equals(getDungeonWorld())) return;
        if (!RETURN_STONE.use(player)) return;

        teleportHome(player);
    }
}
