package net.annova.item.equipment.prison;

import net.annova.item.base.RPGArmor;
import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.Set;

public class PrisonHelmetItem extends RPGItem implements RPGArmor {
    @Override
    public Material type() {
        return Material.CHAINMAIL_HELMET;
    }

    @Override
    public String name() {
        return ChatColor.GRAY + "Шлем охранника";
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
                "&fБыл снят с охранника во время сна"
        );
    }

    @Override
    public Set<Perk> perks() {
        return Set.of();
    }
}
