package net.annova.item.equipment.prison;

import net.annova.item.base.RPGArmor;
import net.annova.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.Set;

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
