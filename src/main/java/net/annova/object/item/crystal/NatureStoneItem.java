package net.annova.object.item.crystal;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class NatureStoneItem extends RPGItem {
    @Override
    public Item type() {
        return Items.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(309f);
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }
}
