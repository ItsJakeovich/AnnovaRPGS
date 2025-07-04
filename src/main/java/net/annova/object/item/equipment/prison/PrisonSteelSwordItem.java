package net.annova.object.item.equipment.prison;

import net.annova.event.custom.BattleCE;
import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.RPGTool;
import net.annova.object.RPGRarity;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static net.annova.object.item.base.ItemType.PRISON_STEEL_SWORD;

public class PrisonSteelSwordItem extends RPGItem implements RPGTool, Listener {
    @Override
    public Item type() {
        return Material.IRON_SWORD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(804f);
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
    public double damage() {
        return 12.5;
    }

    @Override
    public double attackSpeed() {
        return 1.6;
    }

    @EventHandler
    private void hit(BattleCE event) {
        if (!PRISON_STEEL_SWORD.equals(event.getDamager().getEquipment().getItemInMainHand())) return;

        event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300, 0));
    }
}
