package net.annova.item.component;

import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.annova.util.BukkitObjects.chance;

public class ForiumFragmentItem extends RPGItem {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public String name() {
        return "&6Осколок фориума";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{12};
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fИспускает тусклый яркий свет"
        );
    }

    public ItemStack create(RPGPerson person, double chance) {
        double plus = 0;
        for (RPGItem item : person.getRPGArmor()) {
            if (item == null) continue;
            if (item.modelData()[0] != 4) continue;
            plus+=0.0025;
        }
        if (!chance(chance+plus)) return new ItemStack(Material.AIR);
        return super.create();
    }
}
