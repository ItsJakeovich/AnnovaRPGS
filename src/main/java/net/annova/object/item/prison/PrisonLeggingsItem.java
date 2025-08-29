package net.annova.object.item.prison;

import net.annova.object.item.base.attribute.RPGArmor;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

public class PrisonLeggingsItem extends RPGItem implements RPGArmor {
    @Override
    public Item type() {
        return Items.CHAINMAIL_LEGGINGS;
    }

    @Override
    public List<Float> modelData() {
        return List.of(3f);
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
        return 5.5f;
    }

    @Override
    public float toughness() {
        return 2f;
    }

    @Override
    public float speed() {
        return 0;
    }
}
