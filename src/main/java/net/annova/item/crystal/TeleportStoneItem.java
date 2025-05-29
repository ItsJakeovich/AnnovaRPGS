package net.annova.item.crystal;

import net.annova.item.base.RPGItem;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static net.annova.main.GlobalRPG.getDungeonWorld;
import static net.annova.util.BukkitObjects.*;

public class TeleportStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.DARK_PURPLE + "Кристалл телепорта";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{329};
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fМожет создать &1метку",
                "&3= &fПосле чего при использовании телепортирует тебя на &1метку"
        );
    }

    @EventHandler
    private void drop(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (!entity.getType().equals(EntityType.ENDERMAN)) return;
        if (!chance(0.005)) return;

        event.getDrops().add(this.create());
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (!equalItem(item)) return;
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
                    ChatColor.DARK_PURPLE + "Локация: " + loc.getWorld().getName() + ", " + loc.getBlockX()
                    + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
            return;
        }
        List<String> lore = item.getItemMeta().getLore();
        if (!lore.get(0).startsWith(ChatColor.DARK_PURPLE + "Локация: ")) return;
        String[] str = lore.get(0).split(": ")[1].split(", ");

        if (chance(0.5)) {
            this.removeItem(player, item);
        }
        World world = Bukkit.getWorld(str[0]);
        if (world.equals(getDungeonWorld())) return;
        player.setFallDistance(0);
        player.teleport(new Location(Bukkit.getWorld(str[0]), Integer.parseInt(str[1])+0.5, Integer.parseInt(str[2])+0.5, Integer.parseInt(str[3])+0.5));
        player.playSound(player, Sound.ENTITY_ENDER_PEARL_THROW, 1, 1);
    }
}
