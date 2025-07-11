package net.annova.object.item.sector0.oasis;

import net.annova.event.custom.PlayerDamageCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

import static net.annova.object.item.base.ItemType.ANGEL_FISH;

public class AngelFish extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of(10f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.COMMON;
    }

    @EventHandler
    public void onDamage(PlayerDamageCE event) {
        Entity entity = event.getEntity();

        if (entity.getWorld().isClearWeather() && !entity.getLocation().getBlock().getType().equals(Material.WATER)) return;
        if (!ANGEL_FISH.has(entity)) return;

        event.decreaseDamage(event.getBaseDamage() * 0.05);
    }
}
