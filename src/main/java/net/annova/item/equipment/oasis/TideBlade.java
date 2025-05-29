package net.annova.item.equipment.oasis;

import net.annova.entity.mobs.sector0.Crab;
import net.annova.item.base.RPGItem;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.annova.item.base.Item.TIDE_BLADE;
import static net.annova.main.GlobalRPG.getEntity;

public class TideBlade extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.IRON_SWORD;
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
        return List.of("&3= &fКлинок&3, &fкоторый был давно затерян в неизвестных вам водах&3...");
    }

    @EventHandler
    public void attack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity entity) {
            ItemStack item = entity.getEquipment().getItemInMainHand();
            if (!TIDE_BLADE.equals(item)) return;

            if (getEntity(event.getEntity()) instanceof Crab) {
                event.setDamage(event.getDamage() + 3);
            }

            if (entity.getWorld().isClearWeather() && !entity.getLocation().getBlock().getType().equals(Material.WATER)
            ) return;

            event.setDamage(event.getDamage() + 0.9);
        }
    }
}
