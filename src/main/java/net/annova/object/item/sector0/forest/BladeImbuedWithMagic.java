package net.annova.object.item.sector0.forest;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.RPGTool;
import org.bukkit.Material;

import java.util.List;

public class BladeImbuedWithMagic extends RPGItem implements RPGTool {
    @Override
    public Material type() {
        return Material.IRON_SWORD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(417f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public List<String> lore() {
        return List.of();
    }

    @Override
    public double damage() {
        return 8.5;
    }

    @Override
    public double attackSpeed() {
        return 1.6;
    }
}
