package net.annova.item.world0.oasis;

import net.annova.item.base.RPGItem;
import org.bukkit.Material;

import java.util.List;

public class Seasmith extends RPGItem {
    @Override
    public Material type() {
        return Material.SALMON;
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
    public List<String> lore() {
        return List.of("&3= &fМассивная и необычно прочная рыба&3, &fчешуя которой по твёрдости напоминает камень");
    }
}
