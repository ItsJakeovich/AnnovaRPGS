package net.annova.object.item.sector0.oasis;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;

import java.util.List;

public class CrabClaw extends RPGItem {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(429f);
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public List<String> lore() {
        return List.of("&3= &fТвёрдая клешня краба. Ещё тёплая.");
    }
}
