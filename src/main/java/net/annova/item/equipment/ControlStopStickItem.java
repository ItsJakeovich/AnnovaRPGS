package net.annova.item.equipment;

import net.annova.object.person.status.Status;
import net.annova.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.annova.item.base.Item.CONTROL_STOP_STICK;
import static net.annova.main.GlobalRPG.getRPGPerson;

public class ControlStopStickItem extends RPGItem implements Listener {
    @Override
    public Material type() {
        return Material.STICK;
    }

    @Override
    public String name() {
        return "&4Палочка остановки передвижения";
    }

    @Override
    public List<Float> modelData() {
        return List.of(4f);
    }

    @Override
    public boolean unstackable() {
        return false;
    }

    @Override
    public List<String> lore() {
        return List.of(
                "&3= &fПри ударе блокирует передвижение игрока"
        );
    }

    @EventHandler
    public void hit(EntityDamageByEntityEvent conEvent) {
        if (!(conEvent.getDamager() instanceof Player damager)) return;
        if (!(conEvent.getEntity() instanceof Player entity)) return;

        ItemStack item = damager.getInventory().getItemInMainHand();
        if (!CONTROL_STOP_STICK.equals(item)) return;

        RPGPerson person = getRPGPerson(entity);
        if (person.getStatus(Status.Type.STOPPED) == null) this.stop(person);
        else this.unStop(person);
    }

    private void stop(RPGPerson person) {
        person.addStatus(Status.Type.STOPPED);
        person.updateSpeed(0);
        person.getPerson().getPlayer().setFlySpeed(0);
    }
    private void unStop(RPGPerson person) {
        person.removeStatus(Status.Type.STOPPED);
        person.updateSpeed(130);
        person.getPerson().getPlayer().setFlySpeed(0.1f);
    }
}
