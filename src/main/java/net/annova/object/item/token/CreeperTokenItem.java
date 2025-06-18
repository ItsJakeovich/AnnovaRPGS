package net.annova.object.item.token;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.List;
import static net.annova.object.item.base.ItemType.CREEPER_TOKEN;

public class CreeperTokenItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(5f);
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
                "&3= &fКриперы больше на вас не реагируют"
        );
    }

    @EventHandler
    private void mechanic(EntityTargetEvent event) {
        if (!CREEPER_TOKEN.has(event.getTarget())) return;

        if (event.getEntity().getType().equals(EntityType.CREEPER))
            event.setCancelled(true);
    }
}
