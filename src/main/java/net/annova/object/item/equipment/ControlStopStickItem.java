package net.annova.object.item.equipment;

import net.annova.object.RPGRarity;
import net.annova.object.item.base.RPGItem;
import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.annova.object.item.base.ItemType.CONTROL_STOP_STICK;
import static net.annova.main.GlobalRPG.getRPGPerson;
import static net.annova.object.person.status.StatusType.STOPPED;

public class ControlStopStickItem extends RPGItem implements Listener {
    @Override
    public Item type() {
        return Items.STICK;
    }

    @Override
    public List<Float> modelData() {
        return List.of(4f);
    }

    @Override
    public int stackSize() {
        return 1;
    }

    @Override
    public RPGRarity rarity() {
        return RPGRarity.IMPOSSIBLE;
    }

    @EventHandler
    public void hit(EntityDamageByEntityEvent conEvent) {
        if (!(conEvent.getDamager() instanceof Player damager)) return;
        if (!(conEvent.getEntity() instanceof Player entity)) return;

        ItemStack item = damager.getInventory().getItemInMainHand();
        if (!CONTROL_STOP_STICK.equals(item)) return;

        RPGPerson person = getRPGPerson(entity);
        StatusManager manager = person.getStatusManager();
        if (manager.getStatus(STOPPED) == null) this.stop(person, manager);
        else                                    this.unStop(person, manager);
    }

    private void stop(RPGPerson person, StatusManager manager) {
        manager.addStatus(STOPPED);
        person.updateSpeed(0);
        person.getPerson().getPlayer().setFlySpeed(0);
    }
    private void unStop(RPGPerson person, StatusManager manager) {
        manager.removeStatus(STOPPED);
        person.updateSpeed(130);
        person.getPerson().getPlayer().setFlySpeed(0.1f);
    }
}
