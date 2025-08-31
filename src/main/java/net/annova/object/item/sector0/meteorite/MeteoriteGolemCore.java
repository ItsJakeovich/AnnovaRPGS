package net.annova.object.item.sector0.meteorite;

import net.annova.event.custom.DropItemFoundBlockCE;
import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Material;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

import static net.annova.object.item.base.ItemType.METEORITE_GOLEM_CORE;
import static net.minecraft.world.item.Items.NAUTILUS_SHELL;

public class MeteoriteGolemCore extends RPGItem implements Listener {
    @Override
    public Item type() {
        return NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(425f);
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public boolean unusable() {
        return false;
    }

    @EventHandler
    public void drop(DropItemFoundBlockCE event) {
        if (!METEORITE_GOLEM_CORE.equals(event.getItem().getItem())) return;
        CraftBlock block = (CraftBlock) event.getFoundedBlock();
        if (block.isLiquid()) {
            block.getHandle().setBlock(block.getPosition(), Blocks.OBSIDIAN.defaultBlockState(), 3);
            event.getItem().remove(Entity.RemovalReason.DISCARDED);
        }
    }
}
