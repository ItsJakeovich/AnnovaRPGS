package net.annova.item.crystal;

import net.annova.entity.traits.status.Status;
import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.util.BukkitObjects.addItems;
import static net.annova.util.BukkitObjects.chance;
import static net.annova.main.AnnovaRPG.getInstance;

public class RegenerationStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public String name() {
        return ChatColor.LIGHT_PURPLE + "Кристалл регенерации";
    }

    @Override
    public List<Float> modelData() {
        return new int[]{325};
    }

    @Override
    public boolean unstackable() {
        return true;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fПассивно регенерирует &c\uD83D\uDDA4"
        );
    }

    @EventHandler
    private void drop(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (!chance(0.001)) return;
        addItems(player, this.create());
    }

    @EventHandler
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!equalItem(player.getInventory().getItemInMainHand())) return;
        RPGPerson person = getRPGPerson(player);
        if (person.getStatus(Status.Type.REGENERATION) != null) return;
        person.addStatus(Status.Type.REGENERATION);

        player.playSound(player, Sound.BLOCK_PISTON_EXTEND, 1, 1);
        new BukkitRunnable() {
            public void run() {
                if (!hasItem(player)) {
                    cancel();
                }
                if (player.isDead()) return;
                double maxHealth = player.getAttribute(Attribute.MAX_HEALTH).getValue();
                if (player.getHealth() >= maxHealth) return;

                player.setHealth(Math.min(player.getHealth()+2, maxHealth));
            }
        }.runTaskTimer(getInstance(), 0L, 100L);
    }
}
