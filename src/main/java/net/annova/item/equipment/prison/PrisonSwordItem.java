package net.annova.item.equipment.prison;

import net.annova.item.base.RPGItem;
import net.annova.item.base.RPGTool;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.util.BukkitObjects.chance;

public class PrisonSwordItem extends RPGItem implements RPGTool, Listener {
    @Override
    public Material type() {
        return Material.IRON_SWORD;
    }

    @Override
    public String name() {
        return "&7Клинок заключенного";
    }

    @Override
    public List<Float> modelData() {
        return new int []{1};
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
                "&3= &fШанс &616%&f выбить золотой слиток"
        );
    }

    @Override
    public double damage() {
        return 6;
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
    private void mechanic(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof LivingEntity damager)) return;
        if (!this.equalItem(damager.getEquipment().getItemInMainHand())) return;

        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        if (entity.getHealth() - event.getFinalDamage() > 0) return;
        if (!chance(0.16)) return;

        Location loc = entity.getLocation();
        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_INGOT));
    }
}
