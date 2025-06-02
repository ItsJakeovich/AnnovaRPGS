package net.annova.object.item.crystal;

import net.annova.event.custom.BattleCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

import static net.annova.object.item.base.Item.BALANCE_STONE;

public class BalanceStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(316f);
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
                "&3= &fУвеличивает вдвое урон по нежити",
                "&3= &fУменьшает вдвое урон по не-нежити"
        );
    }

    @EventHandler
    private void mechanic(BattleCE event) {
        if (!BALANCE_STONE.has(event.getDamager())) return;

        Entity entity = event.getEntity();
        switch (entity.getType()) {
            case SKELETON:
            case ZOMBIE:
            case HUSK:
            case STRAY:
            case DROWNED:
            case WITHER_SKELETON:
            case ZOMBIFIED_PIGLIN:
            case ZOMBIE_VILLAGER:
                event.increaseDamage(event.getBaseDamage());
                break;
            default:
                event.decreaseDamage(event.getBaseDamage()/2);
        }
    }
}
