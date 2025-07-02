package net.annova.object.item.sector0.oasis;

import net.annova.object.RPGRarity;
import net.annova.object.entity.mobs.sector0.oasis.Crab;
import net.annova.event.custom.BattleCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.RPGTool;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

import static net.annova.object.item.base.ItemType.TIDE_BLADE;
import static net.annova.main.GlobalRPG.getEntity;

public class TideBlade extends RPGItem implements RPGTool, Listener {
    @Override
    public Material type() {
        return Material.IRON_SWORD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(412f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
    }

    @EventHandler
    public void attack(BattleCE event) {
        LivingEntity damager = event.getDamager();
        if (!TIDE_BLADE.equals(damager.getEquipment().getItemInMainHand())) return;

        LivingEntity entity = event.getEntity();
        if (getEntity(entity) instanceof Crab) {
            event.setDamage(event.getDamage() + 3);
        }

        if (damager.getWorld().isClearWeather() && (!entity.isInWater() || !damager.isInWater())) return;

        event.setDamage(event.getDamage() + 0.9);
    }

    @Override
    public double damage() {
        return 6;
    }

    @Override
    public double attackSpeed() {
        return 1.6;
    }
}
