package net.annova.item.equipment.prison;

import net.annova.item.base.RPGArmor;
import net.annova.item.base.RPGItem;
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
    public String name() {
        return ChatColor.GRAY + "Поножи охранника";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{3};
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&fБыли сняты с охранника во время обеда"
        );
    }

    @Override
    public Set<Perk> perks() {
        return Set.of();
    }
}
