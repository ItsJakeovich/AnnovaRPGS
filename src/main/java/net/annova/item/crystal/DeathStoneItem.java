package net.annova.item.crystal;

import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static net.annova.util.BukkitObjects.addItems;
import static net.annova.util.BukkitObjects.chance;

public class DeathStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.DARK_PURPLE + "Кристалл смерти";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{321};
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &6Удваивает весь дроп с мобов",
                "&3= &7Есть шанс быть убитым кристаллом"
        );
    }

    @EventHandler
    private void drop(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.getLastDamageCause() == null) return;
        if (!player.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.WITHER)) return;
        if (!chance(0.01)) return;

        addItems(player, this.create());
    }
    @EventHandler
    private void mechanic(EntityDeathEvent event) {
        if (event.getEntity() instanceof ChestedHorse) return;
        if (event.getEntity() instanceof Player) return;
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;
        if (!hasItem(killer)) return;

        EntityEquipment inv = event.getEntity().getEquipment();
        List<ItemStack> items = new ArrayList<>();
        loop:
        for (ItemStack item : event.getDrops()) {
            if (inv.getItemInMainHand().isSimilar(item)) continue;
            if (inv.getItemInOffHand().isSimilar(item)) continue;
            for (ItemStack armor : inv.getArmorContents()) {
                if (item.isSimilar(armor)) continue loop;
            }

            items.add(item.clone());
        }

        event.getDrops().addAll(items);
    }
}
