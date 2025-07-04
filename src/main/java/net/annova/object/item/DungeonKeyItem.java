package net.annova.object.item;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import org.bukkit.Material;

import java.util.List;

public class DungeonKeyItem extends RPGItem {
    @Override
    public Item type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(3f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }
}
