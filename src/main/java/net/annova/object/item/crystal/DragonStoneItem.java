package net.annova.object.item.crystal;

import net.annova.event.custom.PlayerDamageCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

import static net.annova.object.item.base.ItemType.DRAGON_STONE;

public class DragonStoneItem extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(302f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @EventHandler
    public void onDamage(PlayerDamageCE event) {
        if (DRAGON_STONE.has(event.getPlayer())) {
            event.decreaseDamage(event.getBaseDamage()*0.1);
        }
    }
}
