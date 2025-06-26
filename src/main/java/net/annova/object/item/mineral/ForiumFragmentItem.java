package net.annova.object.item.mineral;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;

import java.util.List;

public class ForiumFragmentItem extends RPGItem {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(12f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.EVENT;
    }

//    public ItemStack create(RPGPerson person, double chance) {
//        double plus = 0;
//        for (RPGItem item : person.getRPGArmor()) {
//            if (item == null) continue;
//            if (item.modelData().getFirst() != 4) continue;
//            plus+=0.0025;
//        }
//        if (!chance(chance+plus)) return new ItemStack(Material.AIR);
//        return ForiumFragmentItem.create();
//    }
}
