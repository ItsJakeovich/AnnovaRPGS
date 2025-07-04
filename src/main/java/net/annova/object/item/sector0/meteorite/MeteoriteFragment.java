package net.annova.object.item.sector0.meteorite;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.minecraft.world.item.Item;
import org.bukkit.Material;

import java.util.List;

public class MeteoriteFragment extends RPGItem {
    @Override
    public Item type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(402f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }
}
