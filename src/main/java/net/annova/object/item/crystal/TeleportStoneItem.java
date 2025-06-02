package net.annova.object.item.crystal;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static net.annova.main.GlobalRPG.getDungeonWorld;
import static net.annova.util.BukkitObjects.*;
import static net.annova.object.item.base.Item.TELEPORT_STONE;

public class TeleportStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(329f);
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
                "&3= &fМожет создать &1метку",
                "&3= &fПосле чего при использовании телепортирует тебя на &1метку"
        );
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (!TELEPORT_STONE.equals(item)) return;
        if (event.getAction().name().charAt(0) != 'R') return;
        Player player = event.getPlayer();
        if (player.getWorld().equals(getDungeonWorld())) return;
        Location loc = player.getLocation();
        int border = switch (loc.getWorld().getEnvironment()) {
            case NORMAL, CUSTOM -> 15000;
            case NETHER, THE_END -> 2500;
        };
        if (loc.getX() > border || loc.getX() < -border || loc.getZ() > border || loc.getZ() < -border) return;
        if (player.isSneaking()) {
            setLore(item,
                    STR."\{ChatColor.DARK_PURPLE}Локация: \{loc.getWorld().getName()}, \{loc.getBlockX()}, \{loc.getBlockY()}, \{loc.getBlockZ()}");
            return;
        }
        List<String> lore = item.getItemMeta().getLore();
        String first = lore.getFirst();
        if (!first.startsWith(STR."\{ChatColor.DARK_PURPLE}Локация: ")) return;
        String[] str = first.split(": ")[1].split(", ");

        if (chance(0.5)) {
            subtractItem(player, item);
        }
        World world = Bukkit.getWorld(str[0]);
        if (world.equals(getDungeonWorld())) return;
        player.setFallDistance(0);
        player.teleport(new Location(Bukkit.getWorld(str[0]), Integer.parseInt(str[1])+0.5, Integer.parseInt(str[2])+0.5, Integer.parseInt(str[3])+0.5));
        player.playSound(player, Sound.ENTITY_ENDER_PEARL_THROW, 1, 1);
    }
}
