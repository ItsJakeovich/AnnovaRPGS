package net.annova.item.crystal;

import net.annova.entity.traits.status.Status;
import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.main.AnnovaRPG.getInstance;

public class IceStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.AQUA + "Ледяной кристалл";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{305};
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fВ радиусе 7 блоков &3замедляет &fигроков"
        );
    }

    @EventHandler
    private void drop(EntityDeathEvent event) {
        if (!event.getEntity().getType().equals(EntityType.STRAY)) return;
        if (!chance(0.005)) return;
        event.getDrops().add(this.create());
    }
    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        if (event.getHand() == null) return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;

        ItemStack item = event.getItem();
        if (!equalItem(item)) return;
        Player player = event.getPlayer();
        RPGPerson person = getRPGPerson(player);

        if (person.getStatus(Status.Type.ICENAGE) != null) return;
        person.addStatus(Status.Type.ICENAGE);
        player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!hasItem(player)) {
                    person.removeStatus(Status.Type.ICENAGE);
                    cancel();
                }
                for (Entity entity : player.getNearbyEntities(7, 5 ,7)) {
                    if (!(entity instanceof LivingEntity ent)) continue;
                    ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 30, 0));
                }
            }
        }.runTaskTimer(getInstance(), 0L, 20L);
    }
}
