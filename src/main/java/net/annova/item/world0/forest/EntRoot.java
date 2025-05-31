package net.annova.item.world0.forest;

import net.annova.item.base.RPGItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

import java.util.List;

public class EntRoot extends RPGItem {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public String name() {
        return STR."\{ChatColor.of("#8B4513")}Корень энта";
    }

    @Override
    public List<Float> modelData() {
        return List.of(215f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of();
    }
}
