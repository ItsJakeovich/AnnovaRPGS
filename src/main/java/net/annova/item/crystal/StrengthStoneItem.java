package net.annova.item.crystal;

import net.annova.event.custom.BattleCE;
import net.annova.item.base.RPGItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

import static net.annova.util.BukkitObjects.addItems;
import static net.annova.util.BukkitObjects.chance;

public class StrengthStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.GOLD + "Кристалл гнева";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{327};
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fУвеличивает входящий урон на 30%",
                "&3= &fУвеличивает наносимый урон на 50%"
        );
    }

    @EventHandler
    private void drop(BattleCE event) {
        if (!(event.getEvent().getDamager() instanceof Player player)) return;
        if (!player.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;
        if (!chance(0.0001)) return;

        addItems(player, this.create());
    }

    @EventHandler
    private void receiver(EntityDamageEvent event) {
        if (!hasItem(event.getEntity())) return;
        event.setDamage(event.getDamage()*1.3);
    }
    @EventHandler
    private void damager(EntityDamageByEntityEvent event) {
        if (!hasItem(event.getDamager())) return;
        event.setDamage(event.getDamage()*1.5);
    }
}
