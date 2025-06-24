package net.annova.object.item.crystal;

import net.annova.event.custom.BattleCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static net.annova.object.item.base.ItemType.BALANCE_STONE;

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
        return RPGRarity.UNCOMMON;
    }

    public static final Set<EntityType> UNDEAD = EnumSet.of(
            EntityType.ZOMBIE,
//            EntityType.ZOMBIE_VILLAGER,
            EntityType.DROWNED,
            EntityType.HUSK,
            EntityType.SKELETON,
            EntityType.STRAY,
            EntityType.WITHER,
            EntityType.ZOGLIN,
            EntityType.ZOMBIFIED_PIGLIN,
            EntityType.PHANTOM
    );
    @EventHandler
    private void mechanic(BattleCE event) {
        if (!BALANCE_STONE.has(event.getDamager())) return;

        Entity entity = event.getEntity();
        if (UNDEAD.contains(event.getEntity().getType()))
             event.increaseDamage(event.getBaseDamage());
        else event.decreaseDamage(event.getBaseDamage()/2);
    }
}
