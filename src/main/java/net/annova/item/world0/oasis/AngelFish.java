package net.annova.item.world0.oasis;

import net.annova.item.base.RPGItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

import static net.annova.item.base.Item.ANGEL_FISH;

public class AngelFish extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.SALMON;
    }

    @Override
    public List<Float> modelData() {
        return List.of();
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of("&3= &fПоявляется только в спокойных водах&3, &fгде никто не причиняет вреда&3. &fЕё присутствие внушает чувство безопасности");
    }

    @EventHandler
    public void onDamage(EntityDamageEv event) {
        Entity entity = event.getEntity();

        if (entity.getWorld().isClearWeather() && !entity.getLocation().getBlock().getType().equals(Material.WATER)) return;
        if (!ANGEL_FISH.has(entity)) return;

        event.setDamage(event.getDamage() * 0.95);
    }
}
