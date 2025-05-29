package net.annova.item.token;

import net.annova.item.base.RPGItem;
import org.bukkit.Material;

import java.util.List;

public class PrisonTokenItem extends RPGItem {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public String name() {
        return "&7Жетон заключения";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{2};
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fНужен для торговли в магазине &8меж-пространства"
        );
    }
}
