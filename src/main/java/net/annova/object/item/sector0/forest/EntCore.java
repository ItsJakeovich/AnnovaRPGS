package net.annova.object.item.sector0.forest;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.minecraft.world.item.Item;
import org.bukkit.Material;

import java.util.List;

public class EntCore extends RPGItem {
    @Override
    public Item type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(431f);
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }
}
