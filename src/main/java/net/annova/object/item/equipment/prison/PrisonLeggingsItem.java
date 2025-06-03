package net.annova.object.item.equipment.prison;

import net.annova.object.item.base.attribute.RPGArmor;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;

import java.util.List;

public class PrisonLeggingsItem extends RPGItem implements RPGArmor {
    @Override
    public Material type() {
        return Material.CHAINMAIL_LEGGINGS;
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
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&fБыли сняты с охранника во время обеда"
        );
    }
}
