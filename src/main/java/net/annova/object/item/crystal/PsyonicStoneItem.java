package net.annova.object.item.crystal;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import org.bukkit.Material;

import java.util.List;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class PsyonicStoneItem extends RPGItem {
    @Override
    public Material type() {
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

    @Override
    public List<String> lore() {
        return List.of(
                "&3| &fСоздает слабое магнитное поле"
        );
    }
}
