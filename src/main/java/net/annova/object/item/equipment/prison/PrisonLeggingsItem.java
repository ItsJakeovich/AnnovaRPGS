package net.annova.object.item.equipment.prison;

import net.annova.object.item.base.attribute.RPGArmor;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import org.bukkit.Material;

import java.util.List;

public class PrisonLeggingsItem extends RPGItem implements RPGArmor {
    @Override
    public Item type() {
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
    public float armor() {
        return 5;
    }

    @Override
    public float toughness() {
        return 0;
    }

    @Override
    public float speed() {
        return 0;
    }
}
