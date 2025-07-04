package net.annova.object.item.crystal;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import org.bukkit.Material;

import java.util.List;

public class GreenStoneItem extends RPGItem {
    @Override
    public Item type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(301f);
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }
}
