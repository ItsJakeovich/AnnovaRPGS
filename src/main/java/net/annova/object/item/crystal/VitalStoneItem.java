package net.annova.object.item.crystal;

import net.annova.event.custom.BattleCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

import static net.annova.object.item.base.Item.VITAL_STONE;

public class VitalStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(323f);
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
                "&3= &fУвеличивает &4⚔ &fна &61.5% &fза каждое &c\uD83D\uDDA4"
        );
    }

    // drop event in net.annova.event.AgeStonesEvent

    @EventHandler
    private void mechanic(BattleCE event) {
        LivingEntity entity = event.getDamager();
        if (!VITAL_STONE.has(entity)) return;

        // damage * (1 + 0.05 * CH);
        // PH = current health.
        event.increaseDamage(event.getBaseDamage() * 0.0075 * entity.getHealth()); // 1 + 0.015 per heart
    }
}
