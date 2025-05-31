package net.annova.item.crystal;

import net.annova.item.base.RPGItem;
import net.annova.util.BukkitObjects;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.annova.environment.dungeon.Loot.random;
import static net.annova.item.base.Item.EARTH_STONE;
import static net.annova.util.BukkitObjects.chance;

public class EarthStoneItem extends RPGItem implements Listener {

    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(306f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fЗаменяет близжайший камень на руду"
        );
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        if (event.getHand() == null) return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;

        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (!EARTH_STONE.use(player, item)) return;

        Material[] materials = {
                Material.IRON_ORE,
                Material.DIAMOND_ORE,
                Material.GOLD_ORE,
                Material.EMERALD_ORE,
                Material.COAL_ORE,
                Material.LAPIS_ORE,
                Material.REDSTONE_ORE
        };
        int radius = 10;
        Location location = player.getLocation();
        World world = location.getWorld();

        for (int y = -radius; y <= radius; y++) {
            for (int z = -radius; z <= radius; z++) {
                for (int x = -radius; x <= radius; x++) {
                    Block block = world.getBlockAt(location.clone().add(x, y, z));
                    if (!block.getType().equals(Material.STONE)) continue;
                    if (!chance(0.084)) continue;
                    block.setType(materials[BukkitObjects.random.nextInt(materials.length)]);
                }
            }
        }
    }
}
