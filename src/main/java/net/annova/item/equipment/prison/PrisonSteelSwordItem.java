package net.annova.item.equipment.prison;

import net.annova.event.custom.BattleCE;
import net.annova.event.custom.EntityDamageByEntityCE;
import net.annova.item.base.RPGItem;
import net.annova.item.base.RPGTool;
import net.annova.object.RPGRarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

import static net.annova.item.base.Item.PRISON_STEEL_SWORD;

public class PrisonSteelSwordItem extends RPGItem implements RPGTool, Listener {
    @Override
    public Material type() {
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
    public List<String> lore() {
        return List.of(
                STR."&4\{this.damage()} ⚔ &7| &e\{this.attackSpeed()} ⚡",
                "",
                "&fБыл создан внутри стен тюрьмы"
        );
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
