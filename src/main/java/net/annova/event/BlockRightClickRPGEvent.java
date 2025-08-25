package net.annova.event;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockRightClickRPGEvent implements Listener {
    @EventHandler
    public void right(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        BlockState block = ((CraftBlock) event.getClickedBlock()).getNMS();
        if (block.is(Blocks.ENCHANTING_TABLE)) event.setCancelled(true);
    }
}
