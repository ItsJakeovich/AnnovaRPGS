package net.annova.object.item.equipment.meteorite;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.RPGArmor;
import net.minecraft.world.item.Item;
import org.bukkit.Material;

import java.util.List;

public class ChitinChestplate extends RPGItem implements RPGArmor {
    @Override
    public Item type() {
        return Material.IRON_CHESTPLATE;
    }

    @Override
    public List<Float> modelData() {
        return List.of(462f);
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
        return 3.3f;
    }

    @Override
    public float toughness() {
        return 1.5f;
    }

    @Override
    public float speed() {
        return -0.02f;
    }
}
