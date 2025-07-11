package net.annova.object.item.sector0.forest;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.Material;

import java.util.List;

public class EntRoot extends RPGItem {
    @Override
    public Item type() {
        return Items.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(401f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }
}
