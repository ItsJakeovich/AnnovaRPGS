package net.annova.object.item.crystal;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class DefenceStoneItem extends RPGItem {
    @Override
    public Item type() {
        return Items.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(324f);
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

    public static final PotionEffect DEFENCE_STONE_RESISTANCE = new PotionEffect(PotionEffectType.RESISTANCE, 120, 1, true, false, true);
    public static final PotionEffect DEFENCE_STONE_SLOWNESS = new PotionEffect(PotionEffectType.SLOWNESS, 120, 0, true, false, true);
}
