package net.annova.item.crystal;

import net.annova.object.person.status.Status;
import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.object.person.status.StatusType.REGENERATION;
import static net.annova.item.base.Item.REGENERATION_STONE;

public class RegenerationStoneItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.AMETHYST_SHARD;
    }

    @Override
    public List<Float> modelData() {
        return List.of(325f);
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
    private void mechanic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!REGENERATION_STONE.equals(player.getInventory().getItemInMainHand())) return;
        RPGPerson person = getRPGPerson(player);
        StatusManager manager = person.getStatusManager();
        if (manager.getStatus(REGENERATION) != null) return;
        manager.addStatus(REGENERATION);

        player.playSound(player, Sound.BLOCK_PISTON_EXTEND, 1, 1);
        new BukkitRunnable() {
            public void run() {
                if (!REGENERATION_STONE.has(player)) {
                    this.cancel();
                }
                if (player.isDead()) return;
                double maxHealth = player.getAttribute(Attribute.MAX_HEALTH).getValue();
                if (player.getHealth() >= maxHealth) return;

                player.setHealth(Math.min(player.getHealth()+2, maxHealth));
            }
        }.runTaskTimer(getInstance(), 0L, 100L);
    }
}
