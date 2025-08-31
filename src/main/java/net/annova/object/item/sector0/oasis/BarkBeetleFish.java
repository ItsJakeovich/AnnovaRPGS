package net.annova.object.item.sector0.oasis;

import net.annova.event.custom.DropItemFoundBlockCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

import static net.annova.event.CapitatorEvent.*;
import static net.annova.object.item.base.ItemType.BARK_BEETLE_FISH;

public class BarkBeetleFish extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(14f);
    }

    @Override
    public int stackSize() {
        return 16;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public boolean unusable() {
        return false;
    }

    @EventHandler
    public void capitatorEvent(DropItemFoundBlockCE event) {
        if (!BARK_BEETLE_FISH.equals(event.getItem().getItem())) return;
        Block block = event.getFoundedBlock();
        if (block.hasMetadata("placedByPlayer")) return;
        if (LOGWOODS.contains(block.getType()))
            if (capitator(event.getPlayer(), block.getLocation()))
                event.getItem().remove(Entity.RemovalReason.DISCARDED);
    }
}
