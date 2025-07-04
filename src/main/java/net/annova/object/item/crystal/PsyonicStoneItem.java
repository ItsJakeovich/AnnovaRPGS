package net.annova.object.item.crystal;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.minecraft.world.item.Item;
import org.bukkit.Material;

import java.util.List;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class PsyonicStoneItem extends RPGItem {
    @Override
    public Item type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(328f);
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
