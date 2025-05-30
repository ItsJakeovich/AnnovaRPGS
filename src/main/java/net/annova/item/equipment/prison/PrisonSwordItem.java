package net.annova.item.equipment.prison;

import net.annova.event.custom.EntityDamageByEntityCE;
import net.annova.item.base.RPGItem;
import net.annova.item.base.RPGTool;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

import static net.annova.item.base.Item.PRISON_SWORD;
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
        return List.of(1f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                STR."&4\{this.damage()} ⚔ &7| &e\{this.attackSpeed()} ⚡",
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
    private void mechanic(EntityDeathEvent event) {
        if (!chance(0.16)) return;
        Player player = event.getEntity().getKiller();
        if (player == null) return;
        if (!PRISON_SWORD.equals(player.getInventory().getItemInMainHand())) return;

        Location loc = event.getEntity().getLocation();
        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_INGOT));
    }
}
