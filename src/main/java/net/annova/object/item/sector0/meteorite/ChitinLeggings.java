package net.annova.object.item.sector0.meteorite;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.RPGArmor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

public class ChitinLeggings extends RPGItem implements RPGArmor {
    @Override
    public Item type() {
        return Items.IRON_LEGGINGS;
    }

    @Override
    public List<Float> modelData() {
        return List.of(463f);
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public boolean unusable() {
        return false;
    }

    @Override
    public float armor() {
        return 2.7f;
    }

    @Override
    public float toughness() {
        return 1.5f;
    }

    @Override
    public float speed() {
        return -0.01f;
    }
}
