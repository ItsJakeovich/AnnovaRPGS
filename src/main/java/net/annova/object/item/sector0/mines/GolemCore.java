package net.annova.object.item.sector0.mines;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.minecraft.world.item.Item;

import java.util.List;

import static net.minecraft.world.item.Items.NAUTILUS_SHELL;

public class GolemCore extends RPGItem {
    @Override
    public Item type() {
        return NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(467f);
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
}
