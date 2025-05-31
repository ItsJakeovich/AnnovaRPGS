package net.annova.item.world0.oasis;

import net.annova.item.base.RPGItem;
import org.bukkit.Material;

import java.util.List;

public class RoyalPufferfish extends RPGItem {
    @Override
    public Material type() {
        return Material.PUFFERFISH;
    }

    @Override
    public List<Float> modelData() {
        return List.of();
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of("&3= &fРедчайший обитатель&3, &fповелитель тропических ливней&3. &fПри его появлении небо будто склоняется перед ним и выпускает дожди &3— &fно сам он остаётся сухим");
    }
}
