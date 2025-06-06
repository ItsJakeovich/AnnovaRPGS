package net.annova.object.item.sector0.dryad;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import org.bukkit.Material;

import java.util.List;

public class WolfsWool extends RPGItem {
    @Override
    public Material type() {
        return null;
    }

    @Override
    public List<Float> modelData() {
        return List.of();
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return null;
    }

    @Override
    public List<String> lore() {
        return List.of();
    }
}
