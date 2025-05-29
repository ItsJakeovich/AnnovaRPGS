package net.annova.item;

import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

public class DungeonKeyItem extends RPGItem {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public String name() {
        return ChatColor.AQUA + "Меж-пространственный ключ";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{3};
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
        );
    }
}
