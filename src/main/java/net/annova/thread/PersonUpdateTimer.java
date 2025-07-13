package net.annova.thread;

import net.annova.object.person.RPGPerson;
import net.annova.object.person.status.StatusManager;
import net.annova.object.person.status.StatusType;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static net.annova.main.GlobalRPG.getRPGPerson;

public class PersonUpdateTimer extends AnnovaTask {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            RPGPerson person = getRPGPerson(player);
            person.updateSectorLocation();

            StatusManager manager = person.getStatusManager();
            for (StatusType type : StatusType.getForActivation()) {
                if (manager.hasStatus(type)) continue;
                if (!type.getActivationItem().has(player)) continue; // если у игрока нет предмета
                manager.addStatus(type);
                player.playSound(player, Sound.ENTITY_WANDERING_TRADER_YES, 1, 1);
            }
            ItemStack item = player.getInventory().getItemInMainHand();
            if (!item.getType().isAir()) {
                for (StatusType type : StatusType.getForActivationInHand()) {
                    if (manager.hasStatus(type)) continue;
                    if (type.getActivationInHandItem().equals(item)) {
                        manager.addStatus(type);
                    }
                }
            }
            person.getEquipment().update();

            person.updateHealth();
            person.regen();

//            long millis = System.currentTimeMillis();
//            for (String modifier : person.getSpeedModifiers().keySet()) {
//                if (millis-person.getSpeedModifier(modifier) < 11000) continue;
//                person.removeSpeedModifier(modifier);
//            }
        }
    }
}
