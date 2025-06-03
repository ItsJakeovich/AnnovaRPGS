package net.annova.object.item.equipment.prison;

import net.annova.object.item.base.RPGItem;
import net.annova.object.item.base.attribute.RPGTool;
import net.annova.object.RPGRarity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.annova.object.item.base.Item.PRISON_SWORD;
import static net.annova.util.BukkitObjects.chance;

public class PrisonSwordItem extends RPGItem implements RPGTool, Listener {
    @Override
    public Material type() {
        return Material.IRON_SWORD;
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
    public RPGRarity rarity() {
        return RPGRarity.UNCOMMON;
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
