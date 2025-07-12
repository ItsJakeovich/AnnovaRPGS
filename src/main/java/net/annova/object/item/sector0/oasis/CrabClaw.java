package net.annova.object.item.sector0.oasis;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

public class CrabClaw extends RPGItem {
    @Override
    public Item type() {
        return Items.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(429f);
    }

    @Override
    public int stackSize() {
        return 7;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }
}
