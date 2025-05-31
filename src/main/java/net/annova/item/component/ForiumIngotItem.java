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
    public List<Float> modelData() {
        return List.of(1f);
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
