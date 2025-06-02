package net.annova.event.action;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static net.annova.util.BukkitObjects.random;

public class HarvestEvent implements Listener {
    @EventHandler
    public void interact(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (!(block.getBlockData() instanceof Ageable ageable)) return;
        if (ageable.getAge() != ageable.getMaximumAge()) return;

        Material mat = switch (block.getType()) {
            case WHEAT -> Material.WHEAT;
            case CARROTS -> Material.CARROT;
            case POTATOES -> Material.POTATO;
            case BEETROOTS -> Material.BEETROOT;
            default -> Material.AIR;
        };
        Material plant = switch (block.getType()) {
            case WHEAT -> Material.WHEAT_SEEDS;
            case BEETROOTS -> Material.BEETROOT_SEEDS;
            default -> Material.AIR;
        };

        Location loc = block.getLocation();
        World world = loc.getWorld();
        world.dropItemNaturally(loc, new ItemStack(mat, random.nextInt(4) + 2));
        world.dropItemNaturally(loc, new ItemStack(plant, random.nextInt(4) + 2));

        ageable.setAge(0);
        block.setBlockData(ageable);

        Player player = event.getPlayer();
        player.playSound(player, Sound.BLOCK_CROP_BREAK, 1, 1);

        ServerPlayer nms = ((CraftPlayer) player).getHandle();
        nms.connection.send(new ClientboundAnimatePacket(nms, 0));
    }
}
