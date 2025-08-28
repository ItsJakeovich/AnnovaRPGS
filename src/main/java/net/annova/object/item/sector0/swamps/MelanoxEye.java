package net.annova.object.item.sector0.swamps;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

public class MelanoxEye extends RPGItem {
    @Override
    public Item type() {
        return Items.SPIDER_EYE;
    }

    @Override
    public List<Float> modelData() {
        return List.of(453f);
    }

    @Override
    public int stackSize() {
        return 4;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public boolean unusable() {
        return false;
    }
}
