package net.annova.event.custom;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DropItemFoundBlockCE extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final ItemStack item;
    private final Block foundedBlock;

    public DropItemFoundBlockCE(Player player, ItemStack item, Block foundedBlock) {
        this.player = player;
        this.item = item;
        this.foundedBlock = foundedBlock;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItem() {
        return item;
    }

    public Block getFoundedBlock() {
        return foundedBlock;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
