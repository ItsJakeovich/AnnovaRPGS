package net.annova.object.item.sector0.oasis;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import org.bukkit.Material;

import java.util.List;

public class RoyalPufferfish extends RPGItem {
    @Override
    public Item type() {
        return Material.PUFFERFISH;
    }

    @Override
    public List<Float> modelData() {
        return List.of(12f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }
}
