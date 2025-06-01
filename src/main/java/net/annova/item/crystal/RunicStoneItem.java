package net.annova.item.crystal;

import net.annova.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class RunicStoneItem extends RPGItem implements Listener {

    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(332f);
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fУдваивает наносимый урон",
                "&3= &fУдваивает получаемый урон"
        );
    }
}
