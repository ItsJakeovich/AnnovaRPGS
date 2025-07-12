package net.annova.object.item.equipment.meteorite;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.RPGArmor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

public class ChitinBoots extends RPGItem implements RPGArmor {
    @Override
    public Item type() {
        return Items.IRON_BOOTS;
    }

    @Override
    public List<Float> modelData() {
        return List.of(464f);
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
    public float armor() {
        return 1.2f;
    }

    @Override
    public float toughness() {
        return 1;
    }

    @Override
    public float speed() {
        return 0.02f;
    }
}
