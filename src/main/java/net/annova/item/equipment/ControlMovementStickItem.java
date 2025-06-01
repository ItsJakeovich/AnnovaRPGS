package net.annova.item.equipment;

import net.annova.object.RPGRarity;
import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.annova.main.AnnovaRPG.getInstance;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.item.base.Item.CONTROL_MOVEMENT_STICK;
import static net.annova.object.person.status.StatusType.FROZEN;

public class ControlMovementStickItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.STICK;
    }

    @Override
    public List<Float> modelData() {
        return List.of(3F);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.IMPOSSIBLE;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fПри ударе забирает контроль у игрока"
        );
    }

    @EventHandler
    public void hit(EntityDamageByEntityEvent conEvent) {
        if (!(conEvent.getDamager() instanceof Player damager)) return;
        if (!(conEvent.getEntity() instanceof Player entity)) return;

        ItemStack item = damager.getInventory().getItemInMainHand();
        if (!CONTROL_MOVEMENT_STICK.equals(item)) return;

        RPGPerson person = getRPGPerson(entity);
        StatusManager manager = person.getStatusManager();
        if (manager.getStatus(FROZEN) == null) this.freeze(person, manager, damager);
        else manager.removeStatus(FROZEN);
    }

    private void freeze(RPGPerson person, StatusManager manager, Player summoner) {
        manager.addStatus(FROZEN);
        Player player = person.getPerson().getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (manager.getStatus(FROZEN) == null) this.cancel();
                if (!player.isOnline()) return;
                if (!summoner.isOnline()) return;
                person.teleport(summoner);
            }
        }.runTaskTimer(getInstance(), 0L, 2L);
    }
}
