package net.annova.item.component;

import net.annova.item.base.RPGItem;
import org.bukkit.Material;

import java.util.List;

public class ForiumIngotItem extends RPGItem {
    @Override
    public Material type() {
        return Material.IRON_INGOT;
    }

    @Override
    public String name() {
        return "&6Слиток фориума";
    }

    @Override
    public List<Float> modelData() {
        return new int[1];
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&fСнеговик теплее будет..."
        );
    }
}
