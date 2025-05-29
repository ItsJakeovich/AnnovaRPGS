package net.annova.item.oasis;

import net.annova.item.base.RPGItem;
import org.bukkit.Material;

import java.util.List;

public class Pearl extends RPGItem {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
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
        return List.of("&3= &fНе совсем рыба&3, &fно часть древней формы жизни&3, &fоставившая после себя сияющее ядро&3. &fТорговцы уважают её как знак удачной сделки и чистой энергии");
    }
}
