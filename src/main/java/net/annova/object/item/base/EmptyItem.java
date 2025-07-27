package net.annova.object.item.base;

import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

public class EmptyItem extends RPGItem {
    @Override
    public Item type() {
        return Items.AIR;
    }

    @Override
    public List<Float> modelData() {
        return List.of();
    }

    @Override
    public int stackSize() {
        return 0;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @Override
    public boolean unusable() {
        return false;
    }
}
