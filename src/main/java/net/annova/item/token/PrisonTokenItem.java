package net.annova.item.token;

import net.annova.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;

import java.util.List;

public class PrisonTokenItem extends RPGItem {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(2f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fНужен для торговли в магазине &8меж-пространства"
        );
    }
}
