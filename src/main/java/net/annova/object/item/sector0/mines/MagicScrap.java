package net.annova.object.item.sector0.mines;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import org.bukkit.Material;

import java.util.List;

import static org.bukkit.Material.NAUTILUS_SHELL;

public class MagicScrap extends RPGItem {
    @Override
    public Material type() {
        return NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(405f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fДовольно пластичный и легко мнущийся материал&3."
        );
    }
}
