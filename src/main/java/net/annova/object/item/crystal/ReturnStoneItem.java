package net.annova.object.item.crystal;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

import static net.annova.object.item.base.ItemType.RETURN_STONE;
import static net.annova.main.GlobalRPG.getDungeonWorld;
import static net.annova.object.person.RPGPerson.teleportHome;
import static net.annova.util.BukkitObjects.addItems;

public class ReturnStoneItem extends RPGItem implements Listener {
    @Override
    public Item type() {
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
        return RPGRarity.UNCOMMON;
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        if (event.getAction().name().charAt(0) != 'R') return;
        Player player = event.getPlayer();
        if (player.getWorld().equals(getDungeonWorld())) return;
        if (!RETURN_STONE.use(player)) return;

        teleportHome(player);
        player.playSound(player, Sound.BLOCK_STONE_BUTTON_CLICK_ON, 2, 2);
    }
}
