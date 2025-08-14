package net.annova.object.item.sector0.forest;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.List;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.item.base.ItemType.DARKNESS_FRAGMENT;

public class DarknessFragment extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(428f);
    }

    @Override
    public int stackSize() {
        return 8;
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
    public void drop(PlayerDropItemEvent event) {
        org.bukkit.entity.Item item = event.getItemDrop();
        if (DARKNESS_FRAGMENT.equals(((CraftItem) item).getHandle().getItem())) {
            event.setCancelled(true);
            item.remove();
        }
    }
}
