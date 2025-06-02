package net.annova.object.item.sector0.oasis;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;

import java.util.List;

public class BlobFishYellow extends RPGItem {
    @Override
    public Material type() {
        return Material.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(6f);
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
        return List.of("&3= &fМрак этой рыбы будто шепчет о буре&3. &fЕё используют шаманы&3, &fкогда жаждут грозы и очищающей силы неба");
    }
}
