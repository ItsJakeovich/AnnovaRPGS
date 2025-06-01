package net.annova.item.crystal;

import net.annova.event.custom.EntityDamageByEntityCE;
import net.annova.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

import static net.annova.item.base.Item.STRENGTH_STONE;

public class StrengthStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(327f);
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
                "&3= &fУвеличивает входящий урон на 30%",
                "&3= &fУвеличивает наносимый урон на 50%"
        );
    }

    @EventHandler
    private void damager(EntityDamageByEntityCE event) {
        if (STRENGTH_STONE.has(event.getEntity())) {
            event.increaseDamage(event.getBaseDamage() * 1.5);
        }
        if (STRENGTH_STONE.has(event.getDamager())) {
            event.increaseDamage(event.getBaseDamage() * 1.3);
        }
    }
}
