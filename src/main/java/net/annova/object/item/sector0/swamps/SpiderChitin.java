package net.annova.object.item.sector0.swamps;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

public class SpiderChitin extends RPGItem {
    @Override
    public Item type() {
        return Items.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(400f);
    }

    @Override
    public int stackSize() {
        return 12;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }
}
