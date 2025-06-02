package net.annova.event;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Snow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.inventory.ItemStack;

import static net.annova.util.BukkitObjects.random;

public class PhysicEvent implements Listener {
    @EventHandler
    public void physics(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.SNOW) {
            if (block.getRelative(BlockFace.DOWN).getType() != Material.AIR) return;
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SNOWBALL,
                    (random.nextInt(3)+1) * ((Snow) block.getBlockData()).getLayers()));
        }
    }
    @EventHandler
    public void piston(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            Block check = block.getRelative(BlockFace.UP);
            if (check.getType() == Material.SNOW) {
                check.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SNOWBALL,
                        (random.nextInt(3)+1) * ((Snow) check.getBlockData()).getLayers()));
            }
        }
    }
}
