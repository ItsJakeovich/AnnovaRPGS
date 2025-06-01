package net.annova.item;

import net.annova.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

public class DungeonKeyItem extends RPGItem {
    @Override
    public Material type() {
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

    @Override
    public List<String> lore() {
        return List.of(
        );
    }
}
