package net.annova.object.item.sector0.oasis;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;

import java.util.List;

public class RoyalPufferfish extends RPGItem {
    @Override
    public Material type() {
        return Material.PUFFERFISH;
    }

    @Override
    public List<Float> modelData() {
        return List.of(12f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public List<String> lore() {
        return List.of("&3= &fРедчайший обитатель&3, &fповелитель тропических ливней&3. &fПри его появлении небо будто склоняется перед ним и выпускает дожди &3— &fно сам он остаётся сухим");
    }
}
