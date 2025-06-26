package net.annova.object.item.mineral;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;

import java.util.List;

public class ForiumIngotItem extends RPGItem {
    @Override
    public Material type() {
        return Material.IRON_INGOT;
    }

    @Override
    public List<Float> modelData() {
        return List.of(1f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.EVENT;
    }
}
