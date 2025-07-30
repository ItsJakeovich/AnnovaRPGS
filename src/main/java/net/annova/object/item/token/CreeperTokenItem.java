package net.annova.object.item.token;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.List;
import static net.annova.object.item.base.ItemType.CREEPER_TOKEN;

public class CreeperTokenItem extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.NAUTILUS_SHELL;
    }

    @Override
    public List<Float> modelData() {
        return List.of(5f);
    }

    @Override
    public int stackSize() {
        return 99;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @Override
    public boolean unusable() {
        return false;
    }

    @EventHandler
    private void mechanic(EntityTargetEvent event) {
        if (!CREEPER_TOKEN.has(event.getTarget())) return;

        if (event.getEntity().getType().equals(EntityType.CREEPER))
            event.setCancelled(true);
    }
}
