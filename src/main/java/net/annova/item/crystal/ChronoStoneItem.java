package net.annova.item.crystal;

import net.annova.event.custom.BattleCE;
import net.annova.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

import static net.annova.item.base.Item.CHRONO_STONE;

public class ChronoStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(331f);
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
                "&3= &fУвеличивает &4⚔ &fна &62% &fза каждое отсутствующее &c\uD83D\uDDA4"
        );
    }

    // drop event in net.annova.event.AgeStonesEvent
    @EventHandler
    private void mechanic(BattleCE event) {
        LivingEntity entity = event.getDamager();
        if (!CHRONO_STONE.has(entity)) return;

        // damage * (1 + 0.05 * PH);
        // PH = losed health.
        event.increaseDamage(event.getBaseDamage() * 0.01 * (entity.getAttribute(Attribute.MAX_HEALTH).getValue() - entity.getHealth()));
    }
}
