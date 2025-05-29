package net.annova.item.equipment.prison;

import net.annova.item.base.RPGItem;
import net.annova.item.base.RPGTool;
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

public class PrisonSteelSwordItem extends RPGItem implements RPGTool, Listener {
    @Override
    public Material type() {
        return Material.IRON_SWORD;
    }

    @Override
    public String name() {
        return ChatColor.BLUE + "Стальной клинок";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{804};
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&4" + damage() + " ⚔ &7| &e" + attackSpeed() + " ⚡",
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

    @Override
    public Set<Perk> perks() {
        return Set.of();
    }

    @EventHandler
    private void hit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager)) return;
        if (!this.equalItem(damager.getInventory().getItemInMainHand())) return;

        if (event.getEntity() instanceof LivingEntity entity) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300, 0));
        }
    }
}
