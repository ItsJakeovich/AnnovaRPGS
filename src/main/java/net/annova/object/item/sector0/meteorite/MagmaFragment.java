package net.annova.object.item.sector0.meteorite;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.minecraft.world.item.Item;
import org.bukkit.event.Listener;

import java.util.List;

import static net.minecraft.world.item.Items.NAUTILUS_SHELL;

public class MagmaFragment extends RPGItem implements Listener {
    @Override
    public Item type() {
        return NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(427f);
    }

    @Override
    public int stackSize() {
        return 8;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }
}
