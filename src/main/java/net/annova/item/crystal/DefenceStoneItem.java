package net.annova.item.crystal;

import net.annova.entity.traits.status.Status;
import net.annova.event.custom.EntityDamageCE;
import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.item.base.Item.DEFENCE_STONE;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.addItems;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.main.AnnovaRPG.getInstance;

public class DefenceStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.BLUE + "Кристалл защиты";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{324};
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fНакладывает пассивно &3эффект сопративления",
                "&3= &fНакладывает пассивно &7эффект замедления"
        );
    }

    @EventHandler
    public void drop(EntityDamageCE event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getBaseDamage() < 3) return;
        if (!chance(0.0001)) return;
        addItems(player, this.create());
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        if (!DEFENCE_STONE.equals(event.getItem())) return;
        Player player = event.getPlayer();
        RPGPerson person = getRPGPerson(player);
        if (person.getStatus(Status.Type.PASSIVE_SHIELD) != null) return;
        person.addStatus(Status.Type.PASSIVE_SHIELD);

        new BukkitRunnable() {
            final PotionEffect resis = new PotionEffect(PotionEffectType.RESISTANCE, 120, 1, true, false, true);
            final PotionEffect slowness = new PotionEffect(PotionEffectType.SLOWNESS, 120, 0, true, false, true);
            public void run() {
                if (!DEFENCE_STONE.has(player)) this.cancel();
                player.addPotionEffect(resis);
                player.addPotionEffect(slowness);
            }
        }.runTaskTimer(getInstance(), 0L, 100L);
    }
}
