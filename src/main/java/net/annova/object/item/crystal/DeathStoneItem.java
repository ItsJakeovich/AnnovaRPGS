package net.annova.object.item.crystal;

import net.annova.object.item.base.RPGItem;
import net.annova.object.RPGRarity;
import org.bukkit.Material;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static net.annova.object.item.base.Item.DEATH_STONE;
import static net.annova.util.BukkitObjects.chance;

public class DeathStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(321f);
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.RARE;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3| &aУдваивает &fвесь дроп с мобов",
                "&3| &fЕсть шанс &cвзрыва &fкристалла"
        );
    }

    @EventHandler
    private void mechanic(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof ChestedHorse) return;
        if (entity instanceof Player) return;

        Player killer = entity.getKiller();
        if (!DEATH_STONE.has(killer)) return;
        if (chance(0.001)) {
            DEATH_STONE.removeItem(killer);
            killer.getWorld().createExplosion(killer.getLocation(),2f, true);
            killer.damage(killer.getLastDamage()*1000);
        }

        EntityEquipment inv = entity.getEquipment();
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
