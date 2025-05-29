package net.annova.item.equipment;

import net.annova.entity.traits.status.Status;
import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
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

public class ControlMovementStickItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.STICK;
    }

    @Override
    public String name() {
        return "&6Палочка передвижения";
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
    public List<String> lore() {
        return List.of(
                "&3= &fПри ударе забирает контроль у игрока"
        );
    }

    @EventHandler
    public void hit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager)) return;
        if (!(event.getEntity() instanceof Player entity)) return;

        ItemStack item = damager.getInventory().getItemInMainHand();
        if (!this.equalItem(item)) return;

        RPGPerson person = getRPGPerson(entity);
        if (person.getStatus(Status.Type.FROZEN) == null) this.freeze(person, damager);
        else person.removeStatus(Status.Type.FROZEN);
    }

    private void freeze(RPGPerson person, Player summoner) {
        person.addStatus(Status.Type.FROZEN);
        Player player = person.getPerson().getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (person.getStatus(Status.Type.FROZEN) == null) this.cancel();
                if (!player.isOnline()) return;
                if (!summoner.isOnline()) return;
                person.teleport(summoner);
            }
        }.runTaskTimer(getInstance(), 0L, 2L);
    }
}
